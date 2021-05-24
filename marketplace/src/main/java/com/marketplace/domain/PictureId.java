package com.marketplace.domain;

import java.util.UUID;

public record PictureId(UUID id) {
    public PictureId {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
    }

    public static PictureId newPictureId() {
       return new PictureId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return id.toString();
    }

    public static PictureId fromString(String uuid) {
        var id = UUID.fromString(uuid);
        return new PictureId(id);
    }
}
