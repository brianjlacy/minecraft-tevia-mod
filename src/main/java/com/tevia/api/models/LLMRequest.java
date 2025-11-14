package com.tevia.api.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a request to an LLM API.
 */
public class LLMRequest {

    private List<Message> messages;
    private String system;
    private int maxTokens;
    private double temperature;

    public LLMRequest() {
        this.messages = new ArrayList<>();
        this.maxTokens = 4096;
        this.temperature = 0.7;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(String role, String content) {
        this.messages.add(new Message(role, content));
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    /**
     * Represents a single message in the conversation.
     */
    public static class Message {
        private String role; // "user" or "assistant"
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
