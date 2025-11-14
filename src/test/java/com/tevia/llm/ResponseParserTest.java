package com.tevia.llm;

import com.tevia.action.models.Action;
import com.tevia.action.models.ChatAction;
import com.tevia.action.models.MovementAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ResponseParser.
 */
class ResponseParserTest {

    private ResponseParser parser;

    @BeforeEach
    void setUp() {
        parser = new ResponseParser();
    }

    @Test
    void testParseSimpleMovement() {
        String response = "[{\"type\": \"move_forward\", \"ticks\": 20}]";

        List<Action> actions = parser.parse(response);

        assertEquals(1, actions.size());
        assertEquals(Action.ActionType.MOVE_FORWARD, actions.get(0).getType());
    }

    @Test
    void testParseMultipleActions() {
        String response = "[" +
                "{\"type\": \"look\", \"yaw\": 45, \"pitch\": 0}," +
                "{\"type\": \"move_forward\", \"ticks\": 20}," +
                "{\"type\": \"jump\"}" +
                "]";

        List<Action> actions = parser.parse(response);

        assertEquals(3, actions.size());
        assertEquals(Action.ActionType.LOOK, actions.get(0).getType());
        assertEquals(Action.ActionType.MOVE_FORWARD, actions.get(1).getType());
        assertEquals(Action.ActionType.JUMP, actions.get(2).getType());
    }

    @Test
    void testParseChatAction() {
        String response = "[{\"type\": \"chat\", \"message\": \"Hello, world!\"}]";

        List<Action> actions = parser.parse(response);

        assertEquals(1, actions.size());
        ChatAction chatAction = (ChatAction) actions.get(0);
        assertEquals("Hello, world!", chatAction.getMessage());
    }

    @Test
    void testParseWithExtraText() {
        String response = "Here are my actions:\n" +
                "[{\"type\": \"move_forward\", \"ticks\": 20}]\n" +
                "I decided to move forward.";

        List<Action> actions = parser.parse(response);

        assertEquals(1, actions.size());
        assertEquals(Action.ActionType.MOVE_FORWARD, actions.get(0).getType());
    }

    @Test
    void testParseEmptyResponse() {
        List<Action> actions = parser.parse("");

        assertEquals(0, actions.size());
    }

    @Test
    void testParseInvalidJson() {
        String response = "This is not valid JSON";

        List<Action> actions = parser.parse(response);

        assertEquals(0, actions.size());
    }

    @Test
    void testParseWaitAction() {
        String response = "[{\"type\": \"wait\"}]";

        List<Action> actions = parser.parse(response);

        // Wait actions are skipped
        assertEquals(0, actions.size());
    }
}
