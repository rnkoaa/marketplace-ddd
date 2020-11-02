package com.marketplace.framework.events;

import java.util.UUID;

public final class ClassifiedAdCreated implements Event {
    private final UUID id;
    private final UUID userId;

    public ClassifiedAdCreated(UUID id, UUID userId) {
        this.id = id;
        this.userId = userId;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }
}
