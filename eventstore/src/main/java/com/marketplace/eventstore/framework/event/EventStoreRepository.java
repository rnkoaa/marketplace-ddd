package com.marketplace.eventstore.framework.event;

import com.marketplace.eventstore.framework.Result;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.marketplace.cqrs.event.Event;
import reactor.core.publisher.Mono;

public interface EventStoreRepository<T, U> {

    List<Event> load(String aggregateName, int fromVersion);

    List<Event> load(U aggregateId, int fromVersion);

    List<Event> load(U aggregateId);

    List<Event> load(String aggregateName);

    Result<Integer> save(U aggregateId, T event);

    Result<Integer> save(T event);

    Result<Integer> save(U aggregateId, List<T> events, int version);

    Result<Integer> save(U aggregateId, T event, int version);

    Result<Integer> save(T event, int version);

    Integer getVersion(U aggregateId);

    Long countEvents(UUID aggregateId);
}
