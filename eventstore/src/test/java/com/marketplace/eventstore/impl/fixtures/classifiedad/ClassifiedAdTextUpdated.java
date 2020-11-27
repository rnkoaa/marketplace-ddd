package com.marketplace.eventstore.impl.fixtures.classifiedad;

import com.marketplace.eventstore.framework.event.Event;

import java.util.UUID;

public class ClassifiedAdTextUpdated implements Event {
  private final UUID id;
  private final UUID aggregateId;
  private final String text;

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
  public UUID aggregateId() {
    return aggregateId;
  }

  public String getText() {
    return text;
  }

  @Override
  public String aggregateName() {
    return ClassifiedAd.class.getSimpleName();
  }
}
