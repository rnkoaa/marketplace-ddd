package com.marketplace.eventstore.framework.event;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
public abstract class EventStreamMetadata {

  public abstract Optional<UUID> getId();

  public abstract Instant getCreatedAt();

  public abstract UUID getAggregateId();

  public abstract String getStreamId();

  public abstract String getAggregateType();

  /*

  public static EventStreamMetadata from(AggregateRoot aggregateRoot) {
    return ImmutableEventStreamMetadata.builder()
        .createdAt(Instant.now())
        .streamId(getStreamId(aggregateRoot))
        .aggregateId(aggregateRoot.getAggregateId())
        .aggregateType(aggregateRoot.getClass().getCanonicalName())
        .build();
  }

  private static String getStreamId(AggregateRoot aggregateRoot) {
    return String.format("%s:%s", aggregateRoot.getClass().getSimpleName(), aggregateRoot.getAggregateId().toString());
  }
   */
}