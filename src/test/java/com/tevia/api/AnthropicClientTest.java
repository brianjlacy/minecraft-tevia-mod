package com.tevia.api;

import com.tevia.api.models.LLMRequest;
import com.tevia.api.models.LLMResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AnthropicClient.
 */
class AnthropicClientTest {

    private AnthropicClient client;

    @BeforeEach
    void setUp() {
        // Initialize with test API key
        client = new AnthropicClient("test-key", "claude-3-5-sonnet-20241022", 30);
    }

    @Test
    void testIsConfigured() {
        assertTrue(client.isConfigured());

        AnthropicClient emptyClient = new AnthropicClient("", "model", 30);
        assertFalse(emptyClient.isConfigured());
    }

    @Test
    void testGetProviderName() {
        assertEquals("anthropic", client.getProviderName());
    }

    @Test
    void testGetModel() {
        assertEquals("claude-3-5-sonnet-20241022", client.getModel());
    }

    @Test
    void testRequestCreation() {
        LLMRequest request = new LLMRequest();
        request.addMessage("user", "Hello");
        request.setSystem("You are a helpful assistant");
        request.setMaxTokens(1000);

        assertEquals(1, request.getMessages().size());
        assertEquals("Hello", request.getMessages().get(0).getContent());
        assertEquals("You are a helpful assistant", request.getSystem());
    }
}
