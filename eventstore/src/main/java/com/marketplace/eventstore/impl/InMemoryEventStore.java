package com.marketplace.eventstore.impl;

import com.marketplace.cqrs.event.Event;
import com.marketplace.eventstore.framework.Result;
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
    public EventStream load(String streamId, int fromVersion) {
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
    public Result<Boolean> append(String streamId, int expectedVersion, List<Event> events) {
        Integer nextVersion = nextVersion(streamId);
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
            return Result.of(true);
        }
        return Result.error(String.format("expected version: %d, actual: %d", expectedVersion, nextVersion));
    }

    @Override
    public Result<Boolean> append(String streamId, int expectedVersion, Event event) {
        int nextVersion = nextVersion(streamId);
        // Concurrency check.
        if ((expectedVersion == 0) || nextVersion == expectedVersion) {
            var entity = EventRecord.fromEvent(streamId, event, expectedVersion);
            List<EventRecord> eventStream = entityStore.get(streamId);
            if (eventStream == null) {
                eventStream = new ArrayList<>();
            }
            eventStream.add(entity);
            entityStore.put(streamId, eventStream);
            return Result.of(true);
        }
        return Result.error(
            String.format("expected version: %d, actual: %d", expectedVersion, nextVersion)
        );
    }

    @Override
    public Long size() {
        return (long) entityStore.size();
    }

    @Override
    public Long streamSize(String streamId) {
        EventStream eventStream = load(streamId);
        return (long) eventStream.size();
    }

    @Override
    public Integer getVersion(String streamId) {
        List<EventRecord> eventStream = entityStore.get(streamId);
        if (eventStream == null || eventStream.isEmpty()) {
            return 0;
        }

        eventStream.sort(Comparator.comparing(EventRecord::getVersion));
        return eventStream.get(eventStream.size() - 1).getVersion();
    }

    @Override
    public Integer nextVersion(String streamId) {
        int currentVersion = getVersion(streamId);
        return ++currentVersion;
    }

    @Override
    public Result<Boolean> publish(String streamId, Event event) {
        var appendResult = append(streamId, 0, event);
        return appendResult.flatmap(res -> {
            if (res) {
                eventPublisher.publish(streamId, event);
                return appendResult;
            }
            return Result.error("unable to publish event.");
        });
    }

    @Override
    public Result<Boolean> publish(String streamId, int expectedVersion, List<Event> events) {
        var appendResult = append(streamId, expectedVersion, events);
        return appendResult.flatmap(res -> {
            if (res) {
                eventPublisher.publish(streamId, events);
                return appendResult;
            }
            return Result.error("unable to publish event.");
        });
    }

    @Override
    public Result<Boolean> publish(String streamId, int expectedVersion, Event event) {
        var appendResult = append(streamId, expectedVersion, event);
        return appendResult.flatmap(res -> {
            if (res) {
                eventPublisher.publish(streamId, event);
                return appendResult;
            }
            return Result.error("unable to publish event.");
        });
    }
}
