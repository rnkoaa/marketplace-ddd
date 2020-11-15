package com.marketplace.domain.shared;

import java.util.UUID;

public record UserId(UUID id) {
    public UserId {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
    }

    @Override
    public String toString() {
       return id.toString();
    }

    public static UserId fromString(String uuid) {
        var id = UUID.fromString(uuid);
        return new UserId(id);
    }
}
