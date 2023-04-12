package com.patricktreppmann.chess.chess.events;

public abstract class Event {
    EventType type;

    public Event(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }
}
