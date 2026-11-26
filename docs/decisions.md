# Design decisions

Short records of the design choices behind the detection model: the context, the decision, why, and what was rejected.

## 1. Fail fast with `requireNonNull` in `PiiMatches`
**Context:** The `PiiMatches` helpers can receive a null list or a null `PiiType` from calling code.
**Decision:** Throw immediately with `Objects.requireNonNull`.
**Why:** A null here is a bug in our own code, not user input. Failing fast points straight at the bug. An empty result would silently mean "no PII found", and unmasked text could reach the AI. The gateway must fail closed.
**Alternative rejected:** Returning `Optional.empty()` or an empty list/map, which hides the bug behind what looks like a valid "no matches" answer.

## 2. `PiiMatch` as a record
**Context:** A match carries a type, start, end and value.
**Decision:** An immutable record with a validating compact constructor.
**Why:** Once validated, a match can never be changed into an invalid state. Accessors, `equals` and `hashCode` come for free.
**Alternative rejected:** A regular class with getters and setters: mutable (it could become invalid after validation) and full of boilerplate.

## 3. Half-open intervals `[start, end)`
**Context:** A match needs its position in the original text.
**Decision:** `start` is inclusive, `end` is exclusive.
**Why:** `text.substring(start, end)` returns exactly the value, length is `end - start`, and adjacent matches don't overlap. It's the same convention as Java's `String` and `List` APIs, so there's no `+1`/`-1` anywhere.
**Alternative rejected:** An inclusive end, which needs `+1` everywhere and invites off-by-one bugs.

## 4. No raw PII in `toString` or exception messages
**Context:** Objects and exceptions end up in logs and stack traces.
**Decision:** `toString` masks the value; exception messages never include it.
**Why:** Logs are stored, copied and read by many people and tools. A PII gateway that leaks PII into its own logs defeats its purpose and creates a privacy-compliance problem.
**Alternative rejected:** The default record `toString`, which prints every field, including the value.

## 5. `PiiMatches` as a static utility class
**Context:** Helpers such as `sortedByStart` operate on lists of matches.
**Decision:** A `final` class with static methods and a private constructor.
**Why:** The helpers are stateless (list in, result out), so there is nothing to instantiate. The private constructor blocks pointless instances. Same pattern as `java.util.Collections`.
**Alternative rejected:** Methods on `PiiMatch` (a single match knows nothing about the list), or a wrapper collection class (more code than currently needed).

## 6. `EnumMap` in `groupByType`
**Context:** Grouping matches by `PiiType`.
**Decision:** Collect into an `EnumMap`.
**Why:** The keys are enum constants. `EnumMap` is backed by an array indexed by the enum's position, so it is compact and fast, and it iterates in declaration order, which makes output predictable.
**Alternative rejected:** The default `HashMap` from `groupingBy`: no guaranteed order and more overhead.

## 7. `Stream.min` in `firstOfType`
**Context:** Finding the earliest match of a given type.
**Decision:** Filter by type, then take the `min` by start.
**Why:** One pass, works on unsorted input, and returns `Optional` when there is no match.
**Alternative rejected:** Sorting and taking the first (slower, n log n), or assuming the input is already sorted (a hidden precondition callers can break).

## 8. Fixed fake value in invalid-offset tests
**Context:** Tests check that invalid start/end pairs are rejected with `IllegalArgumentException`.
**Decision:** Use a fixed value (`dummy@example.com`) instead of building one with `"x".repeat(end - start)`.
**Why:** When `end < start`, `String.repeat` gets a negative count and throws the same `IllegalArgumentException` before the `PiiMatch` constructor runs, so the test would pass even if the offset check were deleted. With a fixed value, the constructor's own check is what throws, because offset checks run before the length check. `example.com` is reserved for examples, so it's never a real address.
**Alternative rejected:** Building the value from the offsets with the `match(start, end)` helper.