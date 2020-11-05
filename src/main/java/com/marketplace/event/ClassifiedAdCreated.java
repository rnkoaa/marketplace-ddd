package com.marketplace.event;

import lombok.Value;

import java.util.UUID;

@Value
public class ClassifiedAdCreated implements Event {
    UUID id;
    UUID userId;

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
