package com.marketplace.eventstore.framework.event;

import com.marketplace.cqrs.event.Event;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class EventStreamImpl implements EventStream<Event> {

  private List<Event> events;
  private final String id;
  private final String name;
  private long version;
  private final Instant createdAt;
  private Instant updatedAt;

  public EventStreamImpl(String id, String name, long version, Instant createdAt) {
    this(id, name, version, createdAt, Instant.now());
  }

  public EventStreamImpl(String id) {
    this(id, "", 0, Instant.now(), Instant.now());
  }

  public EventStreamImpl(String id, String name, long version, List<Event> events) {
    this(id, name, version, Instant.now(), Instant.now());
    this.events = events;
  }

  public EventStreamImpl(
      String id, String name, long version, Instant createdAt, Instant updatedAt) {
    this.id = id;
    this.name = name;
    this.version = version;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.events = new ArrayList<>();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public long getVersion() {
    return version;
  }

  @Override
  public List<Event> getEvents() {
    return events;
  }

  @Override
  public Instant createdAt() {
    return createdAt;
  }

  @Override
  public Instant updatedAt() {
    return updatedAt;
  }

  @Override
  public void append(Event entity, long expectedVersion) {
    this.events.add(entity);
    this.version = expectedVersion;
    this.updatedAt = Instant.now();
  }

  @Override
  public int size() {
    return events.size();
  }
}
