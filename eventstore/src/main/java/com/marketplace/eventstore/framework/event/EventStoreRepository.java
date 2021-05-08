package com.marketplace.eventstore.framework.event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.marketplace.cqrs.event.Event;
import reactor.core.publisher.Mono;

public interface EventStoreRepository<T, U> {

    List<Event> load(U aggregateId, int fromVersion);

    List<Event> load(U aggregateId);

    Optional<Boolean> save(U aggregateId, T event);

    Optional<Boolean> save(T event);

    Optional<Boolean> save(U aggregateId, List<T> events, int version);

    Optional<Boolean> save(U aggregateId, T event, int version);

    Optional<Boolean> save(T event, int version);

    Integer getVersion(U aggregateId);

    Long countEvents(UUID aggregateId);
}
