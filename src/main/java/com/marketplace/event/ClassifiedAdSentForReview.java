package com.marketplace.event;

import lombok.Value;

import java.util.UUID;

@Value
public class ClassifiedAdSentForReview implements Event {
    UUID id;

    public ClassifiedAdSentForReview(UUID id) {
        this.id = id;
    }

    @Override
    public UUID getId() {
        return id;
    }
}
