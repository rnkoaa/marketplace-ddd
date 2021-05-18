package com.marketplace.eventstore.jdbc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.cqrs.event.Event;
import com.marketplace.eventstore.framework.Result;
import com.marketplace.eventstore.framework.event.EventPublisher;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.eventstore.framework.event.EventStream;
import com.marketplace.eventstore.framework.event.EventStreamImpl;
import java.util.List;

public class JdbcEventStoreImpl implements EventStore {

    private final JdbcEventStoreRepository eventStoreRepository;
    private final ObjectMapper objectMapper;
    private final EventPublisher<Event> eventPublisher;

    public JdbcEventStoreImpl(ObjectMapper objectMapper,
        EventPublisher<Event> eventPublisher,
        JdbcEventStoreRepository eventStoreRepository) {
        this.eventStoreRepository = eventStoreRepository;
        this.objectMapper = objectMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public EventStream load(String streamId) {
        List<Event> events = eventStoreRepository.load(streamId);

        if (events == null || events.size() == 0) {
            return new EventStreamImpl(streamId, "", 0, List.of());
        }

        int version = (int) events.get(events.size() - 1).getVersion();
        return new EventStreamImpl(streamId, "", version, events);

    }

    @Override
    public EventStream load(String streamId, int fromVersion) {
        List<Event> events = eventStoreRepository.load(streamId, fromVersion);

        if (events == null || events.size() == 0) {
            return new EventStreamImpl(streamId, "", 0, List.of());
        }

        int version = (int) events.get(events.size() - 1).getVersion();
        return new EventStreamImpl(streamId, "", version, events);
    }

    @Override
    public Result<Boolean> append(String streamId, int expectedVersion, List<Event> events) {
        Result<Integer> result = eventStoreRepository.save(streamId, events, expectedVersion);
        return result.map(size -> size > 0);
    }

    @Override
    public Result<Boolean> append(String streamId, int expectedVersion, Event event) {
        return eventStoreRepository.save(streamId, event, expectedVersion);
    }

    @Override
    public Long size() {
//        return eventStoreRepository.;
        return 0L;
    }

    @Override
    public Long streamSize(String streamId) {
        return eventStoreRepository.countEvents(streamId);
    }

    @Override
    public Integer getVersion(String streamId) {
        return eventStoreRepository.getVersion(streamId);
    }

    @Override
    public Integer nextVersion(String streamId) {
        return eventStoreRepository.nextVersion(streamId);
    }

    @Override
    public Result<Boolean> publish(String streamId, Event event) {
        eventPublisher.publish(streamId, event);
        return Result.of(true);
    }

    @Override
    public Result<Boolean> publish(String streamId, int expectedVersion, List<Event> events) {
        eventPublisher.publish(streamId, events);
        return Result.of(true);
    }

    @Override
    public Result<Boolean> publish(String streamId, int expectedVersion, Event event) {
         eventPublisher.publish(streamId, event);
         return Result.of(true);
    }
}
