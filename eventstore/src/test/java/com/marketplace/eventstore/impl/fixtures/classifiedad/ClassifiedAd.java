package com.marketplace.eventstore.impl.fixtures.classifiedad;

import com.marketplace.eventstore.annotations.AggregateRoot;

import java.util.UUID;

@AggregateRoot
public class ClassifiedAd {
    private final UUID id;
    private final UUID owner;

    public ClassifiedAd(UUID aggregateId, UUID ownerId) {
        this.id = aggregateId;
        this.owner = ownerId;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOwner() {
        return owner;
    }
}
