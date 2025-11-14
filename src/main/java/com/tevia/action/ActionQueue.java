package com.tevia.action;

import com.tevia.action.models.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Manages a queue of actions to be executed.
 */
public class ActionQueue {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionQueue.class);

    private final Queue<Action> queue;
    private final int maxSize;
    private Action currentAction;

    public ActionQueue(int maxSize) {
        this.queue = new LinkedList<>();
        this.maxSize = maxSize;
    }

    /**
     * Add an action to the queue.
     * Returns false if queue is full.
     */
    public synchronized boolean enqueue(Action action) {
        if (queue.size() >= maxSize) {
            LOGGER.warn("Action queue is full, cannot add: {}", action.getDescription());
            return false;
        }

        queue.offer(action);
        LOGGER.debug("Action enqueued: {} (queue size: {})", action.getDescription(), queue.size());
        return true;
    }

    /**
     * Get the next action to execute (without removing it).
     * Returns null if queue is empty.
     */
    public synchronized Action peek() {
        return queue.peek();
    }

    /**
     * Remove and return the next action.
     */
    public synchronized Action dequeue() {
        Action action = queue.poll();
        if (action != null) {
            LOGGER.debug("Action dequeued: {}", action.getDescription());
        }
        return action;
    }

    /**
     * Set the currently executing action.
     */
    public synchronized void setCurrentAction(Action action) {
        this.currentAction = action;
    }

    /**
     * Get the currently executing action.
     */
    public synchronized Action getCurrentAction() {
        return currentAction;
    }

    /**
     * Check if an action is currently being executed.
     */
    public synchronized boolean hasCurrentAction() {
        return currentAction != null && !currentAction.isCompleted();
    }

    /**
     * Clear all actions from the queue.
     */
    public synchronized void clear() {
        queue.clear();
        currentAction = null;
        LOGGER.info("Action queue cleared");
    }

    /**
     * Get the current queue size.
     */
    public synchronized int size() {
        return queue.size();
    }

    /**
     * Check if the queue is empty.
     */
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Check if the queue is full.
     */
    public synchronized boolean isFull() {
        return queue.size() >= maxSize;
    }
}
