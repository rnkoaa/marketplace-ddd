package com.marketplace.eventstore.mongodb;

import com.marketplace.eventstore.framework.event.TypedEvent;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
public interface MongoEventEntity {

  UUID getId();

  UUID getAggregateId();

  String getStreamName();

  List<TypedEvent> getEvents();

  long getVersion();

  Instant getCreatedAt();
}
