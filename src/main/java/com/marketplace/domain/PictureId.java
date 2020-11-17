package com.marketplace.domain;

import com.marketplace.annotations.MongoSingleRecordValue;

import java.util.UUID;

@MongoSingleRecordValue
public record PictureId(UUID id) {
    public PictureId {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
    }

    @Override
    public String toString() {
        return id.toString();
    }

    static PictureId fromString(String uuid) {
        var id = UUID.fromString(uuid);
        return new PictureId(id);
    }
}
