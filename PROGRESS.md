# PII Redaction Gateway - Progress

## Status
- **Current session:** 3 (Collections and streams: List, Map, Set, stream pipelines, Optional)
- **Completed sessions:** 1, 2
- **Open branches / unfinished work:** none
- **Latest release tag:** none yet (v0.1.0 planned at end of Phase 1, after Session 5)
- **Repo:** https://github.com/notaarryan/pii-gateway

## Working agreements
- Aryan writes his own Conventional Commit messages and `git add` commands; Claude reviews before he runs them
- Every session includes the Conventional Commits types table
- Step-by-step guidance during the build ("open this file, do this now"), hint ladder still applies

## Plan changes
- `PiiType` enum and `PiiMatch` record were pulled forward from Session 6 into Session 2 (practice = real code). Session 6 now covers the `Detector` interface (Strategy) plus the email and phone detectors, built on the existing types.
- JUnit 5 + Surefire were added in Session 2. Session 4 now goes deeper: exceptions, `@ParameterizedTest`, `@DisplayName`, lifecycle (`@BeforeEach`), test naming.

## Completed sessions
### Session 1 - Tooling and repo setup (2026-11-16)
- Installed Temurin JDK 21; set JAVA_HOME + PATH in ~/.zshrc so `java` and `mvn` both use 21
- Created Maven project in IntelliJ (groupId `com.aryan`, artifactId `pii-gateway`)
- Set `maven.compiler.release` to 21 in pom.xml (replaced source/target)
- Wrote .gitignore (target/, .env, .idea/, *.iml, .DS_Store, logs) and README
- `git init -b main`, initial commit, created public GitHub repo, pushed
- Branch protection on `main`: require PR, no required approvals (solo), no bypass
- First full cycle: issue #1 → branch `docs/progress-tracking` → commit → PR #2 → squash merge → cleanup

### Session 2 - Java essentials: records, interfaces, generics, enums (2026-11-17)
- Issue #4 → branch `feature/pii-match-model` → 3 atomic commits → PR → squash merge → cleanup
- `chore(build)`: JUnit 5.14.4 via `junit-bom` (import scope), `junit-jupiter` test scope, Surefire 3.6.1 pinned
- `PiiType` enum: EMAIL, PHONE, CREDIT_CARD, SIN, ACCOUNT_NUMBER, PERSON, each with a `tokenLabel` (CARD, ACCOUNT, …)
- `PiiMatch(type, start, end, value)` record: compact constructor validates nulls, start >= 0, end > start, value length == end - start; `end` exclusive; `length()`; `overlaps()` (half-open interval check); `toString()` masks the value; exception messages never contain the value
- 14 tests passing (2 PiiTypeTest, 12 PiiMatchTest)

## Current directory tree
```
pii-gateway/
├── .gitignore
├── PROGRESS.md
├── README.md
├── pom.xml
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/aryan/piigateway/
    │   │       └── detection/
    │   │           ├── PiiMatch.java
    │   │           └── PiiType.java
    │   └── resources/            (empty)
    └── test/
        └── java/
            └── com/aryan/piigateway/
                └── detection/
                    ├── PiiMatchTest.java
                    └── PiiTypeTest.java
```
(`.mvn/` exists locally but is empty, so Git ignores it.)

## Design patterns learned
- **Immutability with records** - `PiiMatch`: final fields, validated once in the compact constructor, so an invalid or modified match can't exist
- Next up: Strategy (Session 6, `Detector`)

## Git skills learned
- `git init -b main`, `git status`, `git add`, `git commit -m`, `git log --oneline`
- `git remote add origin`, `git push -u` (upstream tracking)
- `git switch`, `git switch -c` (create branch), `git pull` before branching
- `git commit --amend` (only on unpushed commits)
- `git branch -D` after a squash merge (new hash, so `-d` refuses), `git fetch --prune`
- Conventional Commits: `type(scope): imperative summary`; types feat, fix, test, docs, refactor, chore, ci, perf, style
- Branch naming: feature/, fix/, chore/, docs/, refactor/, test/
- GitHub Issues + `Closes #N`; PR template (What / Why / How / How to test)
- Merge strategies: merge commit vs squash vs rebase (squash is the project default)
- Branch protection rules vs rulesets
- Staging specific files (not `git add .`) to keep commits atomic
- `git commit` with no `-m` opens the editor (for commits that need a body)
- Pre-PR routine: `git fetch origin && git rebase origin/main`, retest, `git log origin/main..HEAD`, `git diff --stat origin/main...HEAD`
- Rescuing a commit made on the wrong branch: `git switch -c <branch>` then `git switch main && git reset --hard origin/main`
- "Up to date with origin/main" is relative to the last fetch, not live GitHub
- `git ls-files`, `git check-ignore -v`; Git can't see empty folders
- Run `git status` on its own when you need to review before acting (don't chain it)

## Concepts learned
- Maven: pom.xml, standard directory layout, plugins downloaded from Central into ~/.m2
- `modelVersion` (POM format) vs Java version
- `maven.compiler.release` vs `source`/`target`: release compiles against that version's API, turning runtime `NoSuchMethodError` into a compile error
- `java` resolves via PATH; Homebrew `mvn` resolves via JAVA_HOME
- IntelliJ: syncing Maven after editing pom.xml; project SDK and language level; preview vs final language features
- Why `target/` is never committed (generated output: commit inputs, not outputs)
- Test code in src/test/java is never packaged; test-scoped deps stay out of production
- Git tracks files, not folders (empty dirs don't appear; `.gitkeep` convention)
- A green build that compiled nothing proves nothing
- Maven BOM (`dependencyManagement` + `import` scope) supplies versions; Surefire runs `*Test` classes
- Packages are folder paths; `package` declaration must match
- Records: generated constructor, accessors (`x()` not `getX()`), equals/hashCode/toString; fields are final; compact constructor runs your validation, then assigns fields implicitly at the end
- Shallow immutability: `final` freezes the reference, not the object (e.g. a List field)
- Enums: constants call a constructor; constructors are implicitly private; fields + accessors; `name()` is final (stable), `toString()` is overridable (display only)
- Generics: `<T>` type parameters; `Objects.requireNonNull` returns `T` so it can validate and assign in one line; erasure
- Interfaces as contracts (compile-time checked, like TS interfaces)
- JUnit 5: `@Test`, `assertEquals(expected, actual)`, `assertTrue/False`, `assertNotNull`, failure message is the last argument, `assertThrows(Class, lambda)` returns the exception
- Failure (assertion said no) vs Error (unexpected exception)
- A validation test should break exactly one rule and keep everything else valid
- Half-open interval overlap: reason about the two "no overlap" cases and negate (`!(end <= o.start || o.end <= start)`, equivalent to `start < o.end && o.start < end` by De Morgan); overlap is symmetric
- Never put raw PII in `toString()` or exception messages (they end up in logs)

## Shaky (revisit in warm-ups)
- Explaining the **"because"** in answers, not just the fact (still the main gap)
- **Unanswered Session 2 wrap-up questions (ask at the start of Session 3):**
    1. Why is `PiiMatch` a record, and what would break if it were mutable?
    2. Why override `toString()`, and what happens on a PII gateway if you don't?
    3. Why was `start=5, end=3` a weak test for the end check, and what's the general rule?
- `name()` vs `toString()` on enums (answered backwards once)
- `assertThrows` returns the exception (and why that's useful)
- PATH vs JAVA_HOME: `mvn` reads JAVA_HOME, so fix the tool that's wrong
- Rescuing a commit made on the wrong branch
- Commit summaries: imperative, about 50 chars, say what the change gives (not a list of fields)
- Writing PR descriptions (the Session 2 one was written by Claude)
- Reading autocomplete before accepting it (picked `IllegalAccessException` and a stray `com.sun.jdi` import)
- Picking the commit type (docs vs chore): match it to what actually changed

## Current metrics
- None yet (the evaluator arrives in Session 9)
- Tests: 14 passing

## Parked ideas
- Add the Maven Wrapper (`mvnw`) so the project builds without a local Maven install
- Restrict the repo's merge options to squash-only (Settings → General → Pull Requests)
- Decide between `chore(build)` and the `build` commit type, and use one consistently