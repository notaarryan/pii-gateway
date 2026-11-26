package com.aryan.piigateway.detection;

import java.util.List;

public interface Detector {
    /**
     * Finds all matches of this detector's type in the given text.
     *
     * @param text the text to scan; must not be null
     * @return the matches found, ordered by start position; an empty list if there is no PII, never null
     * @throws NullPointerException if text is null
     */
    List<PiiMatch> find(String text);
}
