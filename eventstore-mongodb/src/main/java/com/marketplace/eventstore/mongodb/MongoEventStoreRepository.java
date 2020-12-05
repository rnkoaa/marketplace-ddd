package com.marketplace.eventstore.mongodb;

import com.marketplace.eventstore.framework.event.Event;
import java.util.UUID;

public interface MongoEventStoreRepository extends EventStoreRepository<Event, UUID> {

}
