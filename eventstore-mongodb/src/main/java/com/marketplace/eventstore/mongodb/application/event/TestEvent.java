package com.marketplace.eventstore.mongodb.application.event;

import com.marketplace.eventstore.framework.event.Event;

import java.util.UUID;

public class TestEvent implements Event {

  private UUID id;
  private UUID aggregateId;
  private String title;
  private String text;

  public TestEvent() {}

  public TestEvent(UUID id, UUID aggregateId, String title, String text) {
    this.id = id;
    this.aggregateId = aggregateId;
    this.title = title;
    this.text = text;
  }

  public static TestEvent of(UUID eventId, UUID aggregateId, String title) {
    return new TestEvent(eventId, aggregateId, title, "");
  }
  public static TestEvent of(UUID aggregateId, String title) {
    return new TestEvent(UUID.randomUUID(), aggregateId, title, "");
  }

  public static TestEvent of(UUID aggregateId, String title, String text) {
    return new TestEvent(UUID.randomUUID(), aggregateId, title, text);
  }

  public static TestEvent of(UUID id, UUID aggregateId, String title, String text) {
    return new TestEvent(id, aggregateId, title, text);
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public UUID getAggregateId() {
    return aggregateId;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setAggregateId(UUID aggregateId) {
    this.aggregateId = aggregateId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
