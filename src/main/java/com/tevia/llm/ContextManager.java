package com.tevia.llm;

import com.tevia.api.models.LLMRequest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Manages conversation history context for the LLM.
 * Implements a sliding window to keep only recent messages.
 */
public class ContextManager {

    private final LinkedList<LLMRequest.Message> conversationHistory;
    private final int maxMessages;

    public ContextManager(int maxMessages) {
        this.maxMessages = maxMessages;
        this.conversationHistory = new LinkedList<>();
    }

    /**
     * Add a user message to the conversation history.
     */
    public void addUserMessage(String content) {
        addMessage("user", content);
    }

    /**
     * Add an assistant message to the conversation history.
     */
    public void addAssistantMessage(String content) {
        addMessage("assistant", content);
    }

    /**
     * Add a message to the conversation history.
     */
    private void addMessage(String role, String content) {
        conversationHistory.addLast(new LLMRequest.Message(role, content));

        // Remove oldest messages if we exceed max
        while (conversationHistory.size() > maxMessages) {
            conversationHistory.removeFirst();
        }
    }

    /**
     * Get the conversation history.
     */
    public List<LLMRequest.Message> getConversationHistory() {
        return new ArrayList<>(conversationHistory);
    }

    /**
     * Clear the conversation history.
     */
    public void clear() {
        conversationHistory.clear();
    }

    /**
     * Get the number of messages in history.
     */
    public int size() {
        return conversationHistory.size();
    }
}
