package com.tevia.perception;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Tracks recent events for context.
 */
public class EventTracker {

    private static final int MAX_EVENTS = 10;

    private final LinkedList<String> events;

    public EventTracker() {
        this.events = new LinkedList<>();
    }

    /**
     * Record an event.
     */
    public void recordEvent(String event) {
        events.addFirst(event);

        // Keep only recent events
        while (events.size() > MAX_EVENTS) {
            events.removeLast();
        }
    }

    /**
     * Get list of recent events (newest first).
     */
    public List<String> getRecentEvents() {
        return new ArrayList<>(events);
    }

    /**
     * Clear all events.
     */
    public void clearEvents() {
        events.clear();
    }

    // Convenience methods for common events

    public void recordDamage(float amount, String source) {
        recordEvent(String.format("Took %.1f damage from %s", amount, source));
    }

    public void recordItemPickup(String item, int count) {
        recordEvent(String.format("Picked up %dx %s", count, item));
    }

    public void recordChatMessage(String sender, String message) {
        recordEvent(String.format("Chat from %s: %s", sender, message));
    }

    public void recordBlockBroken(String block) {
        recordEvent(String.format("Broke %s", block));
    }

    public void recordBlockPlaced(String block) {
        recordEvent(String.format("Placed %s", block));
    }

    public void recordEntityKilled(String entity) {
        recordEvent(String.format("Killed %s", entity));
    }
}
