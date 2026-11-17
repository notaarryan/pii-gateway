package com.aryan.piigateway.detection;

public enum PiiType {
    EMAIL("EMAIL"),
    PHONE("PHONE"),
    CREDIT_CARD("CARD"),
    SIN("SIN"),
    ACCOUNT_NUMBER("ACCOUNT"),
    PERSON("PERSON");
    private final String tokenLabel;

    PiiType(String tokenLabel) {
        this.tokenLabel = tokenLabel;
    }

    public String tokenLabel() {
        return this.tokenLabel;
    }
}
