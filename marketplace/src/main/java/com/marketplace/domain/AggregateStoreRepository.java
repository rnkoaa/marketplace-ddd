package com.marketplace.domain;

import com.marketplace.cqrs.event.EventId;
import com.marketplace.cqrs.event.VersionedEvent;
import com.marketplace.cqrs.framework.AggregateRoot;
import com.marketplace.domain.repository.Repository;
import com.marketplace.eventstore.framework.Result;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.eventstore.framework.event.EventStream;
import com.marketplace.eventstore.jdbc.EventClassCache;
import io.vavr.control.Try;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.jooq.DSLContext;

@Named
@Singleton
public class AggregateStoreRepository
    /* Repository<AggregateRoot<EventId, VersionedEvent>, EventId>*/ {

    private final EventClassCache aggregateTypeMapper;

    private final EventStore<VersionedEvent> eventEventStore;

    @Inject
    public AggregateStoreRepository(
        DSLContext dslContext,
        EventStore<VersionedEvent> eventEventStore) {
        this.eventEventStore = eventEventStore;
        this.aggregateTypeMapper = EventClassCache.getInstance(dslContext);
    }

    public boolean exists(EventId id) {
        EventStream<VersionedEvent> eventStream = eventEventStore.load(id.getStreamId());
        return !eventStream.isEmpty();
    }

    public Optional<AggregateRoot<EventId, VersionedEvent>> load(EventId id) {
        EventStream<VersionedEvent> eventStream = eventEventStore.load(id.getStreamId());
        if (eventStream.isEmpty()) {
            return Optional.empty();
        }

        String aggregateName = id.getAggregateName();
        return aggregateTypeMapper.get(aggregateName)
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

    public Optional<AggregateRoot<EventId, VersionedEvent>> add(AggregateRoot<EventId, VersionedEvent> aggregateRoot) {
        aggregateTypeMapper.put(aggregateRoot.getClass());
        List<VersionedEvent> changes = aggregateRoot.getChanges();
        Result<Boolean> publishResult = eventEventStore
            .publish(aggregateRoot.getStreamId(), aggregateRoot.getVersion(), changes);
        return publishResult.toOptional()
            .filter(it -> it)
            .map(res -> {
                aggregateRoot.clearChanges();
                return aggregateRoot;
            });
    }

    public void deleteAll() {
        System.out.println("Deleting all events");
        eventEventStore.deleteAll();
    }
}
