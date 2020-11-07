package com.marketplace.domain;

import java.util.UUID;

public record ClassifiedAdId(UUID id) {
    public ClassifiedAdId {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
    }

    public ClassifiedAdId() {
        this(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return id.toString();
    }

    static ClassifiedAdId fromString(String uuid) {
        var id = UUID.fromString(uuid);
        return new ClassifiedAdId(id);
    }

}
