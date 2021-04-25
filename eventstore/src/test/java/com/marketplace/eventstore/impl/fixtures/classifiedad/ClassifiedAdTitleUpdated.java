package com.marketplace.eventstore.impl.fixtures.classifiedad;

import com.marketplace.cqrs.event.Event;

import java.util.UUID;

public class ClassifiedAdTitleUpdated implements Event {
  private final UUID id;
  private final String title;
  private final UUID aggregateId;

  public ClassifiedAdTitleUpdated(UUID id, UUID aggregateId, String title) {
    this.id = id;
    this.title = title;
    this.aggregateId = aggregateId;
  }

  public ClassifiedAdTitleUpdated(UUID aggregateId, String title) {
    this(UUID.randomUUID(), aggregateId, title);
  }

  public ClassifiedAdTitleUpdated(String aggregateId, String title) {
    this(UUID.randomUUID(), UUID.fromString(aggregateId), title);
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public UUID getAggregateId() {
    return aggregateId;
  }

  public String getTitle() {
    return title;
  }

  @Override
  public String getAggregateName() {
    return ClassifiedAd.class.getSimpleName();
  }
}
