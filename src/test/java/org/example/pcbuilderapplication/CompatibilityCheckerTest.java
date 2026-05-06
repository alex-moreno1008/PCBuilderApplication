package org.example.pcbuilderapplication;

import org.example.pcbuilderapplication.CompatibilityChecker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CompatibilityCheckerTest {

    @Test
    void socketsMatch_shouldReturnTrue_whenSameSocket() {
        assertTrue(CompatibilityChecker.socketsMatch("AM4", "AM4"));
    }

    @Test
    void socketsMatch_shouldReturnFalse_whenDifferentSocket() {
        assertFalse(CompatibilityChecker.socketsMatch("AM4", "LGA1700"));
    }

    @Test
    void ramTypesMatch_shouldReturnTrue_whenSameType() {
        assertTrue(CompatibilityChecker.ramTypesMatch("DDR5", "DDR5"));
    }

    @Test
    void ramTypesMatch_shouldReturnFalse_whenDifferentType() {
        assertFalse(CompatibilityChecker.ramTypesMatch("DDR4", "DDR5"));
    }

    @Test
    void socketsMatch_shouldReturnFalse_whenSocketIsNull() {
        String nullSocket = null;
        assertFalse(CompatibilityChecker.socketsMatch(nullSocket, "AM4"));
    }

    @Test
    void ramTypesMatch_shouldReturnFalse_whenRamTypeIsNull() {
        String nullRamType = null;
        assertFalse(CompatibilityChecker.ramTypesMatch(nullRamType, "DDR4"));
    }
}