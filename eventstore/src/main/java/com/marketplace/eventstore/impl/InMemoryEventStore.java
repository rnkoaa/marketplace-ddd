package com.marketplace.eventstore.impl;

import com.marketplace.eventstore.framework.OperationResult;
import com.marketplace.eventstore.framework.OperationResult.Failure;
import com.marketplace.eventstore.framework.OperationResult.Success;
import com.marketplace.eventstore.framework.event.*;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryEventStore implements EventStore<Event> {
  private final EventPublisher<Event> eventPublisher;

  private final Map<String, List<EventRecord>> entityStore = new HashMap<>();

  public InMemoryEventStore(EventPublisher<Event> eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Override
  public EventStream<Event> load(String streamId) {
    List<EventRecord> eventStream = entityStore.get(streamId);
    if (eventStream == null || eventStream.size() == 0) {
      return new EventStreamImpl(streamId, "", 0, List.of());
    }

    eventStream.sort(Comparator.comparing(EventRecord::getVersion));

    List<Event> events =
        eventStream.stream().map(EventRecord::getEvent).collect(Collectors.toList());

    int version = eventStream.get(eventStream.size() - 1).getVersion();
    return new EventStreamImpl(streamId, "", version, events);
  }

  @Override
  public EventStream<Event> load(String streamId, int fromVersion) {
    List<EventRecord> eventStream = entityStore.get(streamId);
    if (eventStream == null || eventStream.size() == 0) {
      return new EventStreamImpl(streamId, "", 0, List.of());
    }

    eventStream.sort(Comparator.comparing(EventRecord::getVersion));
    int version = eventStream.get(eventStream.size() - 1).getVersion();

    List<Event> events =
        eventStream.stream()
            .dropWhile(it -> it.getVersion() < fromVersion)
            .map(EventRecord::getEvent)
            .collect(Collectors.toList());

    return new EventStreamImpl(streamId, "", version, events);
  }

  @Override
  public OperationResult append(String streamId, int expectedVersion, List<Event> events) {
    int currentVersion = getVersion(streamId);
    int nextVersion = currentVersion + 1;
    if ((expectedVersion == 0) || nextVersion == expectedVersion) {
      int numberOfEvents = events.size();
      List<EventRecord> eventStream = entityStore.get(streamId);
      if (eventStream == null) {
        eventStream = new ArrayList<>();
      }
      for (int index = 0; index < numberOfEvents; index++) {
        Event event = events.get(index);
        EventRecord eventEntity = EventRecord.fromEvent(streamId, event, expectedVersion + index);
        eventStream.add(eventEntity);
      }
      entityStore.put(streamId, eventStream);
      return new Success();
    }
    return new Failure(
        String.format("expected version: %d, actual: %d", expectedVersion, nextVersion));
  }

  @Override
  public OperationResult append(String streamId, int expectedVersion, Event event) {
    int currentVersion = getVersion(streamId);
    int nextVersion = currentVersion + 1;

    // Concurrency check.
    if ((expectedVersion == 0) || nextVersion == expectedVersion) {
      var entity = EventRecord.fromEvent(streamId, event, expectedVersion);
      List<EventRecord> eventStream = entityStore.get(streamId);
      if (eventStream == null) {
        eventStream = new ArrayList<>();
      }
      eventStream.add(entity);
      entityStore.put(streamId, eventStream);
      return new Success();
    }
    return new Failure(
        String.format("expected version: %d, actual: %d", expectedVersion, nextVersion));
  }

  @Override
  public int size() {
    return entityStore.size();
  }

  @Override
  public int streamSize(String streamId) {
    return load(streamId).size();
  }

  @Override
  public int getVersion(String streamId) {
    List<EventRecord> eventStream = entityStore.get(streamId);
    if (eventStream == null || eventStream.isEmpty()) {
      return 0;
    }

    eventStream.sort(Comparator.comparing(EventRecord::getVersion));
    return eventStream.get(eventStream.size() - 1).getVersion();
  }

  @Override
  public int nextVersion(String streamId) {
    int currentVersion = getVersion(streamId);
    return ++currentVersion;
  }

  @Override
  public OperationResult publish(String streamId, Event event) {
    var appendResult = append(streamId, 0, event);
    if (Success.matches(appendResult)) {
      eventPublisher.publish(streamId, event);
    }
    return appendResult;
  }

  @Override
  public OperationResult publish(String streamId, int expectedVersion, List<Event> events) {
    var appendResult = append(streamId, expectedVersion, events);
    if (Success.matches(appendResult)) {
      eventPublisher.publish(streamId, events);
    }
    return appendResult;
  }

  @Override
  public OperationResult publish(String streamId, int expectedVersion, Event event) {
    var appendResult = append(streamId, expectedVersion, event);
    if (Success.matches(appendResult)) {
      eventPublisher.publish(streamId, event);
    }
    return appendResult;
  }
}
