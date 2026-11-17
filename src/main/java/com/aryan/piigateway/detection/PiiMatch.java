package com.aryan.piigateway.detection;

import java.util.Objects;

public record PiiMatch(PiiType type, int start, int end, String value) {
    public PiiMatch {
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(value, "value must not be null");
        if (start < 0) {
            throw new IllegalArgumentException("start cannot be less than 0 was: " + start);
        }
        if (end <= start) {
            throw new IllegalArgumentException("end cannot be less than or equal to start, [end:" + end + ", start:" + start + "]");
        }
        if (value.length() != end - start) {
            throw new IllegalArgumentException("length of value has to be equal to end-start was: " + value.length() + " required length: " + (end - start));
        }
    }

    public int length() {
        return end - start;
    }

    public boolean overlaps(PiiMatch other) {
        return !(this.end <= other.start || other.end <= this.start);
    }

    @Override
    public String toString() {
        return "PiiMatch[type=" + type + ", start=" + start + ", end=" + end + ", value=***]";
    }
}

