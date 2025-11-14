package com.tevia.action.models;

/**
 * Chat actions.
 */
public class ChatAction extends Action {

    private String message;

    public ChatAction() {
        super(ActionType.SEND_CHAT);
    }

    public ChatAction(String message) {
        super(ActionType.SEND_CHAT);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getDescription() {
        return "Send chat: " + message;
    }

    public static ChatAction send(String message) {
        return new ChatAction(message);
    }
}
