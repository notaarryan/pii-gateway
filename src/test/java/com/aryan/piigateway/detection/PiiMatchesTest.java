package com.aryan.piigateway.detection;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PiiMatchesTest {
    private static PiiMatch match(PiiType type, int start, int end) {
        return new PiiMatch(type, start, end, "x".repeat(end-start));
    }

    @Test
    void sortedByStart_sortsOutOfOrderInput() {
        List<PiiMatch> matches = new ArrayList<>(List.of(match(PiiType.CREDIT_CARD, 20, 30), match(PiiType.CREDIT_CARD, 0, 10), match(PiiType.CREDIT_CARD, 10, 20)));
        List<PiiMatch> result = PiiMatches.sortedByStart(matches);

        assertEquals(List.of(0, 10, 20), result.stream().map(PiiMatch::start).toList());
    }

    @Test
    void sortedByStart_doesNotModifyInputList() {
        List<PiiMatch> matches = new ArrayList<>(List.of(match(PiiType.CREDIT_CARD, 20, 30), match(PiiType.CREDIT_CARD, 0, 10), match(PiiType.CREDIT_CARD, 10, 20)));

        PiiMatches.sortedByStart(matches);

        assertEquals(List.of(20,0,10), matches.stream().map(PiiMatch::start).toList());
    }

    @Test
    void sortedByStart_returnsEmptyListForEmptyInput() {
        List<PiiMatch> result = PiiMatches.sortedByStart(List.of());
        assertTrue(result.isEmpty());
    }
}
