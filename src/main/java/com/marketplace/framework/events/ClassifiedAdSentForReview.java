package com.marketplace.framework.events;

import java.util.UUID;

public final class ClassifiedAdSentForReview implements Event {
    private final UUID id;

    public ClassifiedAdSentForReview(UUID id) {
        this.id = id;
    }

    @Override
    public UUID getId() {
        return id;
    }
}
