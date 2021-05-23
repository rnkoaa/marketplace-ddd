package com.marketplace.domain;

import com.marketplace.cqrs.event.EventId;
import com.marketplace.cqrs.event.VersionedEvent;
import com.marketplace.cqrs.framework.AggregateRoot;

public abstract class BaseCommandRepository<T extends AggregateRoot<EventId, VersionedEvent>> {

    protected String getStreamIdByClass(Class<?> clzz, String aggregateId) {
        return String.format("%s:%s", clzz.getSimpleName(), aggregateId);
    }

    protected String getStreamId(T object, String aggregateId) {
        return String.format("%s:%s", object.getClass().getSimpleName(), aggregateId);
    }

}
