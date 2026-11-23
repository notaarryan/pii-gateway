package com.aryan.piigateway.detection;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PiiMatchesTest {
    private static PiiMatch match(PiiType type, int start, int end) {
        return new PiiMatch(type, start, end, "x".repeat(end-start));
    }

    @Test
    void sortedByStart_sortsOutOfOrderInput() {
        List<PiiMatch> matches = (List.of(match(PiiType.CREDIT_CARD, 20, 30), match(PiiType.CREDIT_CARD, 0, 10), match(PiiType.CREDIT_CARD, 10, 20)));
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

    @Test
    void groupByType_groupsMatchesUnderTheirType() {
        List<PiiMatch> matches = (List.of(match(PiiType.EMAIL, 0, 10), match(PiiType.EMAIL, 10, 20), match(PiiType.PHONE, 20, 30)));
        Map<PiiType, List<PiiMatch>> result = PiiMatches.groupByType(matches);
        assertEquals(List.of(match(PiiType.EMAIL, 0, 10), match(PiiType.EMAIL, 10, 20)), result.get(PiiType.EMAIL));
        assertEquals(List.of(match(PiiType.PHONE, 20, 30)), result.get(PiiType.PHONE));
    }

    @Test
    void groupByType_keysFollowEnumOrder() {
        List<PiiMatch> matches = (List.of(match(PiiType.PHONE, 0, 10), match(PiiType.EMAIL, 10, 20)));
        Map<PiiType, List<PiiMatch>> result = PiiMatches.groupByType(matches);
        assertEquals(List.of(PiiType.EMAIL, PiiType.PHONE), new ArrayList<>(result.keySet()));
    }

    @Test
    void groupByType_omitsTypesWithNoMatches() {
        List<PiiMatch> matches = (List.of(match(PiiType.EMAIL, 0, 10), match(PiiType.EMAIL, 10, 20)));
        Map<PiiType, List<PiiMatch>> result = PiiMatches.groupByType(matches);
        assertFalse(result.containsKey(PiiType.SIN));
    }

    @Test
    void groupByType_returnsEmptyMapForEmptyInput() {
        Map<PiiType, List<PiiMatch>> result = PiiMatches.groupByType(List.of());
        assertTrue(result.isEmpty());
    }

    @Test
    void firstOfType_returnsEarliestMatchOfType_regardlessOfInputOrder() {
        List<PiiMatch> matches = List.of(match(PiiType.EMAIL, 20, 25), match(PiiType.PHONE, 0, 10), match(PiiType.EMAIL, 5, 10));
        Optional<PiiMatch> result = PiiMatches.firstOfType(matches, PiiType.EMAIL);
        assertEquals(Optional.of(match(PiiType.EMAIL, 5, 10)), result);
    }

    @Test
    void firstOfType_returnsEmpty_whenNoMatchOfType() {
        List<PiiMatch> matches = List.of(match(PiiType.EMAIL, 0, 5), match(PiiType.PHONE, 10, 20));
        Optional<PiiMatch> result = PiiMatches.firstOfType(matches, PiiType.SIN);
        assertEquals(Optional.empty(), result);
    }

    @Test
    void firstOfType_returnsEmpty_forEmptyList() {
        List<PiiMatch> matches = List.of();
        Optional<PiiMatch> result = PiiMatches.firstOfType(matches, PiiType.EMAIL);
        assertEquals(Optional.empty(), result);
    }
}
