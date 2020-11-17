package com.marketplace.domain.shared;

import com.marketplace.annotations.MongoSingleRecordValue;

import java.util.UUID;

@MongoSingleRecordValue
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
