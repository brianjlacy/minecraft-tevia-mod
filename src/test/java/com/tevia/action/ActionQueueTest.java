package com.tevia.action;

import com.tevia.action.models.ChatAction;
import com.tevia.action.models.MovementAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ActionQueue.
 */
class ActionQueueTest {

    private ActionQueue queue;

    @BeforeEach
    void setUp() {
        queue = new ActionQueue(5);
    }

    @Test
    void testEnqueueAndDequeue() {
        MovementAction action = MovementAction.forward(20);

        assertTrue(queue.enqueue(action));
        assertEquals(1, queue.size());

        MovementAction dequeued = (MovementAction) queue.dequeue();
        assertNotNull(dequeued);
        assertEquals(0, queue.size());
    }

    @Test
    void testQueueFull() {
        // Fill the queue
        for (int i = 0; i < 5; i++) {
            assertTrue(queue.enqueue(MovementAction.forward(10)));
        }

        // Try to add one more
        assertFalse(queue.enqueue(MovementAction.forward(10)));
        assertTrue(queue.isFull());
    }

    @Test
    void testClear() {
        queue.enqueue(MovementAction.forward(10));
        queue.enqueue(ChatAction.send("Hello"));

        assertEquals(2, queue.size());

        queue.clear();

        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
    }

    @Test
    void testCurrentAction() {
        MovementAction action = MovementAction.forward(20);

        assertFalse(queue.hasCurrentAction());

        queue.setCurrentAction(action);
        assertTrue(queue.hasCurrentAction());
        assertEquals(action, queue.getCurrentAction());

        action.setCompleted(true);
        assertFalse(queue.hasCurrentAction());
    }
}
