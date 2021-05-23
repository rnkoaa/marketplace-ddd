package com.marketplace.eventstore.jdbc;

import com.marketplace.cqrs.event.Event;
import com.marketplace.cqrs.event.VersionedEvent;
import com.marketplace.eventstore.framework.Result;
import com.marketplace.eventstore.framework.event.EventPublisher;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.eventstore.framework.event.EventStream;
import com.marketplace.eventstore.framework.event.VersionedEventStreamImpl;
import java.util.List;

public class JdbcEventStoreImpl implements EventStore<VersionedEvent> {

    private final JdbcEventStoreRepository eventStoreRepository;
    private final EventPublisher<Event> eventPublisher;

    public JdbcEventStoreImpl(
        EventPublisher<Event> eventPublisher,
        JdbcEventStoreRepository eventStoreRepository) {
        this.eventStoreRepository = eventStoreRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public EventStream<VersionedEvent> load(String streamId) {
        List<VersionedEvent> events = eventStoreRepository.load(streamId);

        if (events == null || events.size() == 0) {
            return new VersionedEventStreamImpl(streamId, "", 0, List.of());
        }

        return new VersionedEventStreamImpl(streamId, "", getVersion(streamId), events);

    }

    @Override
    public EventStream<VersionedEvent> load(String streamId, int fromVersion) {
        List<VersionedEvent> events = eventStoreRepository.load(streamId, fromVersion);

        if (events == null || events.size() == 0) {
            return new VersionedEventStreamImpl(streamId, "", 0, List.of());
        }

        return new VersionedEventStreamImpl(streamId, "", getVersion(streamId), events);
    }

    @Override
    public Result<Boolean> append(String streamId, int expectedVersion, List<VersionedEvent> events) {
        Result<Integer> result = eventStoreRepository.save(streamId, events, expectedVersion);
        return result.map(size -> size > 0);
    }

    @Override
    public Result<Boolean> append(String streamId, int expectedVersion, VersionedEvent event) {
        return eventStoreRepository.save(streamId, event, expectedVersion);
    }

    @Override
    public Long size() {
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
    public Result<Boolean> publish(String streamId, VersionedEvent event) {
        eventPublisher.publish(streamId, event);
        return Result.of(true);
    }

    @Override
    public Result<Boolean> publish(String streamId, int expectedVersion, List<VersionedEvent> events) {
        Result<Integer> filter = eventStoreRepository.save(streamId, events, expectedVersion)
            .filter(res -> res != null && res > 0);

        if (filter.isPresent()) {
            events.forEach(event -> eventPublisher.publish(streamId, event));
            return Result.of(true);
        }

        return Result.error("error publishing events");
    }

    @Override
    public Result<Boolean> publish(String streamId, int expectedVersion, VersionedEvent event) {
        Result<Boolean> save = eventStoreRepository.save(streamId, event, expectedVersion);
        return save.map(res -> {
            eventPublisher.publish(streamId, event);
            return true;
        });
    }
}
