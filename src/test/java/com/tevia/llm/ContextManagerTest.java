package com.tevia.llm;

import com.tevia.api.models.LLMRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ContextManager.
 */
class ContextManagerTest {

    private ContextManager contextManager;

    @BeforeEach
    void setUp() {
        contextManager = new ContextManager(5);
    }

    @Test
    void testAddMessages() {
        contextManager.addUserMessage("Hello");
        contextManager.addAssistantMessage("Hi there!");

        List<LLMRequest.Message> history = contextManager.getConversationHistory();

        assertEquals(2, history.size());
        assertEquals("user", history.get(0).getRole());
        assertEquals("Hello", history.get(0).getContent());
        assertEquals("assistant", history.get(1).getRole());
    }

    @Test
    void testSlidingWindow() {
        // Add more than max messages
        for (int i = 0; i < 10; i++) {
            contextManager.addUserMessage("Message " + i);
        }

        // Should only keep last 5
        assertEquals(5, contextManager.size());

        List<LLMRequest.Message> history = contextManager.getConversationHistory();
        assertEquals("Message 5", history.get(0).getContent());
    }

    @Test
    void testClear() {
        contextManager.addUserMessage("Hello");
        contextManager.addAssistantMessage("Hi");

        assertEquals(2, contextManager.size());

        contextManager.clear();

        assertEquals(0, contextManager.size());
    }
}
