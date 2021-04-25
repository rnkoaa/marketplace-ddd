package com.marketplace.domain.shared;

import java.util.UUID;

public record UserId(UUID id) {

    public UserId {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
    }

    public static UserId newId() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId from(UUID id) {
        return new UserId(id);
    }

    public static UserId from(String uuid) {
        return new UserId(UUID.fromString(uuid));
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
