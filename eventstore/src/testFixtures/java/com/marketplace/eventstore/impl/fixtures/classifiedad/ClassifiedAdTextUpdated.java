package com.marketplace.eventstore.impl.fixtures.classifiedad;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.marketplace.cqrs.event.Event;

import java.util.UUID;

public class ClassifiedAdTextUpdated implements Event {
  private final UUID id;
  private final UUID aggregateId;
  private final String text;

  @JsonCreator
  public ClassifiedAdTextUpdated(UUID id, UUID aggregateId, String text) {
    this.id = id;
    this.text = text;
    this.aggregateId = aggregateId;
  }

  public ClassifiedAdTextUpdated(UUID aggregateId, String text) {
    this(UUID.randomUUID(), aggregateId, text);
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public UUID getAggregateId() {
    return aggregateId;
  }

  public String getText() {
    return text;
  }

  @Override
  public String getAggregateName() {
    return ClassifiedAd.class.getSimpleName();
  }

  @Override
  public String getStreamId() {
    return String.format("%s:%s", getAggregateName(), getAggregateId());
  }
}
