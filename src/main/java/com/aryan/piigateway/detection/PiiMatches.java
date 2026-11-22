package com.aryan.piigateway.detection;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class PiiMatches {
    private PiiMatches() {}

    public static List<PiiMatch> sortedByStart(List<PiiMatch> matches) {
        return matches.stream().sorted(Comparator.comparingInt(PiiMatch::start)).toList();
    }

    public static Map<PiiType, List<PiiMatch>> groupByType(List<PiiMatch> matches) {
        return matches.stream().collect(Collectors.groupingBy(PiiMatch::type, () -> new EnumMap<>(PiiType.class), Collectors.toList()));
    }
}
