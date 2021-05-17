package com.marketplace.eventstore.jdbc;

import com.marketplace.cqrs.event.Event;
import com.marketplace.eventstore.framework.event.EventStoreRepository;
import java.util.UUID;

public interface JdbcEventStoreRepository extends EventStoreRepository<UUID> {

}
