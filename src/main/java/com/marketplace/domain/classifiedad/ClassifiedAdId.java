package com.marketplace.domain.classifiedad;

import com.marketplace.annotations.MongoSingleRecordValue;

import java.util.UUID;

@MongoSingleRecordValue
public record ClassifiedAdId(UUID id) {
    public ClassifiedAdId {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
    }

    public ClassifiedAdId() {
        this(UUID.randomUUID());
    }

    public static ClassifiedAdId newClassifedAdId() {
        return new ClassifiedAdId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return id.toString();
    }

    public static ClassifiedAdId fromString(String uuid) {
        var id = UUID.fromString(uuid);
        return new ClassifiedAdId(id);
    }

}
