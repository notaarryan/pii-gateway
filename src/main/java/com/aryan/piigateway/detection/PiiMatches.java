package com.aryan.piigateway.detection;

import java.util.Comparator;
import java.util.List;

public final class PiiMatches {
    private PiiMatches() {}

    public static List<PiiMatch> sortedByStart(List<PiiMatch> matches) {
        return matches.stream().sorted(Comparator.comparingInt(PiiMatch::start)).toList();
    }
}
