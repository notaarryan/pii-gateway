package com.aryan.piigateway.detection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PiiMatchTest {
    @Test
    void validMatchHasCorrectLength() {
        PiiMatch match = new PiiMatch(PiiType.EMAIL, 0, 5, "a@b.c");
        assertEquals(5, match.length());
    }

    @Test
    void nullTypeIsRejected() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new PiiMatch(null, 0, 1, "x"));
        assertTrue(exception.getMessage().contains("type"));
    }

    @Test
    void nullValueIsRejected() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new PiiMatch(PiiType.EMAIL, 0, 5, null));
        assertTrue(exception.getMessage().contains("value"));
    }

    @Test
    void negativeStartIsRejected() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new PiiMatch(PiiType.EMAIL, -1, 4, "a.b@c"));
    }

    @Test
    void endNotAfterStartIsRejected() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new PiiMatch(PiiType.EMAIL, 5, 5, ""));
        assertTrue(exception.getMessage().contains("5"));
    }

    @Test
    void valueLengthMismatchIsRejected() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new PiiMatch(PiiType.EMAIL, 0, 5, "a.b"));
        assertTrue(exception.getMessage().contains("3"));
    }

    @Test
    void partialOverlap() {
        PiiMatch a = match(0, 5);
        PiiMatch b = match(3, 8);
        assertTrue(a.overlaps(b));
        assertTrue(b.overlaps(a));
    }

    @Test
    void containment() {
        PiiMatch a = match(0, 10);
        PiiMatch b = match(2, 4);
        assertTrue(a.overlaps(b));
        assertTrue(b.overlaps(a));
    }

    @Test
    void adjacentDoesNotOverlap() {
        PiiMatch a = match(0, 5);
        PiiMatch b = match(5, 9);
        assertFalse(a.overlaps(b));
        assertFalse(b.overlaps(a));
    }

    @Test
    void separateDoesNotOverlap() {
        PiiMatch a = match(0, 3);
        PiiMatch b = match(6, 9);
        assertFalse(a.overlaps(b));
        assertFalse(b.overlaps(a));
    }

    @Test
    void toStringHidesValue() {
        PiiMatch match = new PiiMatch(PiiType.CREDIT_CARD, 0, 16, "4111111111111111");
        assertFalse(match.toString().contains("4111111111111111"));
    }

    @Test
    void equalComponentsAreEqual() {
        PiiMatch match1 = new PiiMatch(PiiType.CREDIT_CARD, 0, 16, "4111111111111111");
        PiiMatch match2 = new PiiMatch(PiiType.CREDIT_CARD, 0, 16, "4111111111111111");
        assertEquals(match1, match2);
    }

    private static PiiMatch match(int start, int end) {
        return new PiiMatch(PiiType.EMAIL, start, end, "x".repeat(end - start));
    }
}
