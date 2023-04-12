package com.patricktreppmann.chess.chess.events;

public class MessageEvent extends Event {
    private final String message;
    private final String sender;

    public MessageEvent(String message, String sender ) {
        super(EventType.MESSAGE);
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}
