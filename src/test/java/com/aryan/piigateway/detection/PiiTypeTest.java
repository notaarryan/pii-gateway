package com.aryan.piigateway.detection;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PiiTypeTest {
    @Test
    void creditCardUsesCardLabel() {
        String label = PiiType.CREDIT_CARD.tokenLabel();
        assertEquals("CARD", label);
    }

    @Test
    void everyTypeHasNonBlankLabel() {
        PiiType[] types = PiiType.values();
        for (PiiType type: types) {
            String label = type.tokenLabel();
            assertNotNull(label, type.name());
            assertFalse(label.isBlank(), type.name());
        }
    }
}
