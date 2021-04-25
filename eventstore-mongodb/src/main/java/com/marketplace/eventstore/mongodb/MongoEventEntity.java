package com.marketplace.eventstore.mongodb;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.marketplace.cqrs.event.TypedEvent;
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
