package com.aryan.piigateway.detection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class PiiMatchTest {
    @Test
    @DisplayName("Valid matches have correct length")
    void validMatchHasCorrectLength() {
        PiiMatch match = new PiiMatch(PiiType.EMAIL, 0, 5, "a@b.c");
        assertEquals(5, match.length());
    }

    @Test
    @DisplayName("Null type is rejected")
    void nullTypeIsRejected() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new PiiMatch(null, 0, 1, "x"));
        assertTrue(exception.getMessage().contains("type"));
    }

    @Test
    @DisplayName("Null value is rejected")
    void nullValueIsRejected() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new PiiMatch(PiiType.EMAIL, 0, 5, null));
        assertTrue(exception.getMessage().contains("value"));
    }

    @ParameterizedTest(name = "start={0}, end={1}")
    @CsvSource({"-1,4", "5,5", "6,3"})
    @DisplayName("Illegal start end arguments are rejected")
    void illegalStartEndArgumentsAreRejected(int start, int end) {
        var ex = assertThrows(IllegalArgumentException.class, () -> new PiiMatch(PiiType.EMAIL, start, end, "dummy@example.com"));
        assertFalse(ex.getMessage().contains("dummy@example.com"));
    }

    @Test
    @DisplayName("Value and length mismatch is rejected")
    void valueLengthMismatchIsRejected() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new PiiMatch(PiiType.EMAIL, 0, 5, "a.b"));
        assertTrue(exception.getMessage().contains("3"));
        assertFalse(exception.getMessage().contains("a.b"));
    }

    @Test
    @DisplayName("Partially overlapping matches overlap")
    void partialOverlap() {
        PiiMatch a = match(0, 5);
        PiiMatch b = match(3, 8);
        assertTrue(a.overlaps(b));
        assertTrue(b.overlaps(a));
    }

    @Test
    @DisplayName("A match inside another overlap")
    void containment() {
        PiiMatch a = match(0, 10);
        PiiMatch b = match(2, 4);
        assertTrue(a.overlaps(b));
        assertTrue(b.overlaps(a));
    }

    @Test
    @DisplayName("Adjacent matches do not overlap")
    void adjacentDoesNotOverlap() {
        PiiMatch a = match(0, 5);
        PiiMatch b = match(5, 9);
        assertFalse(a.overlaps(b));
        assertFalse(b.overlaps(a));
    }

    @Test
    @DisplayName("Separate matches do not overlap")
    void separateDoesNotOverlap() {
        PiiMatch a = match(0, 3);
        PiiMatch b = match(6, 9);
        assertFalse(a.overlaps(b));
        assertFalse(b.overlaps(a));
    }

    @Test
    @DisplayName("toString method hides value")
    void toStringHidesValue() {
        PiiMatch match = new PiiMatch(PiiType.CREDIT_CARD, 0, 16, "4111111111111111");
        assertFalse(match.toString().contains("4111111111111111"));
    }

    @Test
    @DisplayName("Equal components are equal")
    void equalComponentsAreEqual() {
        PiiMatch match1 = new PiiMatch(PiiType.CREDIT_CARD, 0, 16, "4111111111111111");
        PiiMatch match2 = new PiiMatch(PiiType.CREDIT_CARD, 0, 16, "4111111111111111");
        assertEquals(match1, match2);
    }

    private static PiiMatch match(int start, int end) {
        return new PiiMatch(PiiType.EMAIL, start, end, "x".repeat(end - start));
    }
}
