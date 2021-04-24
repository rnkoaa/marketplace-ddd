package com.marketplace.eventstore.mongodb;

import com.marketplace.cqrs.event.Event;

import java.util.UUID;

public interface MongoEventStoreRepository extends EventStoreRepository<Event, UUID> {

}
