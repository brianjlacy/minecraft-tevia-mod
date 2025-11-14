package com.tevia.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TeviaConfig.
 */
class TeviaConfigTest {

    private TeviaConfig config;

    @BeforeEach
    void setUp() {
        config = new TeviaConfig();
    }

    @Test
    void testDefaultValues() {
        assertEquals("anthropic", config.getApiProvider());
        assertEquals(0.5, config.getDecisionFrequencyHz());
        assertEquals(16, config.getPerceptionRadius());
        assertTrue(config.isEnableEntityDetection());
        assertTrue(config.isEnableCombat());
    }

    @Test
    void testSettersAndGetters() {
        config.setApiProvider("replicate");
        config.setDecisionFrequencyHz(1.0);
        config.setPerceptionRadius(32);

        assertEquals("replicate", config.getApiProvider());
        assertEquals(1.0, config.getDecisionFrequencyHz());
        assertEquals(32, config.getPerceptionRadius());
    }

    @Test
    void testValidation() {
        config.setApiProvider("anthropic");
        config.setAnthropicApiKey("test-key");

        assertDoesNotThrow(() -> config.validate());
    }

    @Test
    void testValidationFailsWithInvalidProvider() {
        config.setApiProvider("invalid");

        assertThrows(IllegalStateException.class, () -> config.validate());
    }

    @Test
    void testValidationFailsWithoutApiKey() {
        config.setApiProvider("anthropic");
        config.setAnthropicApiKey("");

        assertThrows(IllegalStateException.class, () -> config.validate());
    }
}
