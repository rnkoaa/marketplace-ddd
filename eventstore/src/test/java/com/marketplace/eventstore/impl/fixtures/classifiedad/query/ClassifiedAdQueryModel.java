package com.marketplace.eventstore.impl.fixtures.classifiedad.query;

import java.util.UUID;

public class ClassifiedAdQueryModel {
    private final UUID owner;
    private final UUID id;

    public ClassifiedAdQueryModel(UUID aggregateId, UUID owner) {
        this.id = aggregateId;
        this.owner = owner;
    }

    public UUID getOwner() {
        return owner;
    }

    public UUID getId() {
        return id;
    }
}
