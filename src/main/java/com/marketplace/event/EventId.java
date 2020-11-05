package com.marketplace.event;

import java.util.UUID;

public record EventId(UUID id) {
    public EventId {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
    }

    @Override
    public String toString() {
        return id.toString();
    }

    static EventId fromString(String uuid) {
        return new EventId(UUID.fromString(uuid));
    }
}
