package com.marketplace.domain;

import com.marketplace.cqrs.event.EventId;
import com.marketplace.cqrs.event.VersionedEvent;
import com.marketplace.cqrs.framework.AggregateRoot;
import com.marketplace.domain.repository.Repository;
import com.marketplace.eventstore.framework.Result;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.eventstore.framework.event.EventStream;
import io.vavr.control.Try;
import java.util.List;
import java.util.Optional;

public class AggregateStoreRepository implements
    Repository<AggregateRoot<EventId, VersionedEvent>, EventId> {

    private final AggregateTypeMapper aggregateTypeMapper = AggregateTypeMapper.getInstance();

    private final EventStore<VersionedEvent> eventEventStore;

    public AggregateStoreRepository(
        EventStore<VersionedEvent> eventEventStore) {
        this.eventEventStore = eventEventStore;
    }

    @Override
    public boolean exists(EventId id) {
        EventStream<VersionedEvent> eventStream = eventEventStore.load(id.getStreamId());
        return !eventStream.isEmpty();
    }

    @Override
    public Optional<AggregateRoot<EventId, VersionedEvent>> load(EventId id) {
        EventStream<VersionedEvent> eventStream = eventEventStore.load(id.getStreamId());
        if (eventStream.isEmpty()) {
            return Optional.empty();
        }

        String aggregateName = id.getAggregateName();
        return aggregateTypeMapper.getClassInfo(aggregateName)
            .flatMap(clzz -> Try.of(clzz::getDeclaredConstructor))
            .flatMap(constructor -> Try.of(constructor::newInstance))
            .map(instance -> {
                @SuppressWarnings("unchecked")
                AggregateRoot<EventId, VersionedEvent> agg = (AggregateRoot<EventId, VersionedEvent>) instance;
                agg.load(eventStream.getEvents());
                return agg;
            })
            .toJavaOptional();
    }

    @Override
    public Optional<AggregateRoot<EventId, VersionedEvent>> add(AggregateRoot<EventId, VersionedEvent> aggregateRoot) {
        aggregateTypeMapper.put(aggregateRoot);
        List<VersionedEvent> changes = aggregateRoot.getChanges();
        aggregateRoot.getStreamId();
        Result<Boolean> publishResult = eventEventStore
            .publish(aggregateRoot.getStreamId(), aggregateRoot.getVersion(), changes);
        return publishResult.toOptional()
            .filter(it -> it)
            .map(res -> {
                aggregateRoot.clearChanges();
                return aggregateRoot;
            });
    }
}
