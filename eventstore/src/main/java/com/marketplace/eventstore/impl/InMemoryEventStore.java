package com.marketplace.eventstore.impl;

import com.marketplace.cqrs.event.Event;
import com.marketplace.eventstore.framework.OperationResult;
import com.marketplace.eventstore.framework.OperationResult.Failure;
import com.marketplace.eventstore.framework.OperationResult.Success;
import com.marketplace.eventstore.framework.event.*;

import java.util.*;
import java.util.stream.Collectors;
import reactor.core.publisher.Mono;

public class InMemoryEventStore implements EventStore<Event> {

  private final EventPublisher<Event> eventPublisher;

  private final Map<String, List<EventRecord>> entityStore = new HashMap<>();

  public InMemoryEventStore(EventPublisher<Event> eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Override
  public Mono<EventStream> load(String streamId) {
    List<EventRecord> eventStream = entityStore.get(streamId);
    if (eventStream == null || eventStream.size() == 0) {
      return Mono.just(new EventStreamImpl(streamId, "", 0, List.of()));
    }

    eventStream.sort(Comparator.comparing(EventRecord::getVersion));

    List<Event> events =
        eventStream.stream().map(EventRecord::getEvent).collect(Collectors.toList());

    long version = eventStream.get(eventStream.size() - 1).getVersion();
    return Mono.just(new EventStreamImpl(streamId, "", version, events));
  }

  @Override
  public Mono<EventStream> load(String streamId, long fromVersion) {
    List<EventRecord> eventStream = entityStore.get(streamId);
    if (eventStream == null || eventStream.size() == 0) {
      return Mono.just(new EventStreamImpl(streamId, "", 0, List.of()));
    }

    eventStream.sort(Comparator.comparing(EventRecord::getVersion));
    long version = eventStream.get(eventStream.size() - 1).getVersion();

    List<Event> events =
        eventStream.stream()
            .dropWhile(it -> it.getVersion() < fromVersion)
            .map(EventRecord::getEvent)
            .collect(Collectors.toList());

    return Mono.just(new EventStreamImpl(streamId, "", version, events));
  }

  @Override
  public Mono<OperationResult> append(String streamId, long expectedVersion, List<Event> events) {
    return getVersion(streamId)
        .map(currentVersion -> {
          long nextVersion = currentVersion + 1;

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
          return new Failure(String.format("expected version: %d, actual: %d", expectedVersion, nextVersion));
        });
  }

  @Override
  public Mono<OperationResult> append(String streamId, long expectedVersion, Event event) {
    return getVersion(streamId)
        .map(currentVersion -> {
          long nextVersion = currentVersion + 1;

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
        });
  }

  @Override
  public Mono<Long> size() {
    return Mono.just((long) entityStore.size());
  }

  @Override
  public Mono<Long> streamSize(String streamId) {
    return load(streamId).map(eventStream -> (long) eventStream.size());
  }

  @Override
  public Mono<Long> getVersion(String streamId) {
    List<EventRecord> eventStream = entityStore.get(streamId);
    if (eventStream == null || eventStream.isEmpty()) {
      return Mono.just(-1L);
    }

    eventStream.sort(Comparator.comparing(EventRecord::getVersion));
    return Mono.just(eventStream.get(eventStream.size() - 1).getVersion());
  }

  @Override
  public Mono<Long> nextVersion(String streamId) {
    return getVersion(streamId).map(currentVersion -> ++currentVersion);
  }

  @Override
  public Mono<OperationResult> publish(String streamId, Event event) {
    var appendResult = append(streamId, 0, event);
    return appendResult.flatMap(result -> {
      if (Success.matches(result)) {
        eventPublisher.publish(streamId, event);
      }
      return appendResult;
    });
  }

  @Override
  public Mono<OperationResult> publish(String streamId, long expectedVersion, List<Event> events) {
    var appendResult = append(streamId, expectedVersion, events);
    return appendResult.flatMap(result -> {
      if (Success.matches(result)) {
        eventPublisher.publish(streamId, events);
      }
      return appendResult;
    });
  }

  @Override
  public Mono<OperationResult> publish(String streamId, long expectedVersion, Event event) {
    var appendResult = append(streamId, expectedVersion, event);
    return appendResult.flatMap(result -> {
      if (Success.matches(result)) {
        eventPublisher.publish(streamId, event);
      }
      return appendResult;
    });
  }
}
