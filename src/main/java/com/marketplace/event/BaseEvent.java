package com.marketplace.event;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseEvent implements Event {
    private final Instant createdAt;
    private final UUID id;

    protected BaseEvent(UUID id, Instant createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public UUID getId() {
        return id;
    }
}
