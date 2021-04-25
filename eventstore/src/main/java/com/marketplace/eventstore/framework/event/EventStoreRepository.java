package com.marketplace.eventstore.framework.event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.marketplace.cqrs.event.Event;
import reactor.core.publisher.Mono;

public interface EventStoreRepository<T, U> {

  Mono<List<Event>> load(U aggregateId, int fromVersion);

  Mono<List<Event>> load(U aggregateId);

  Mono<Optional<Boolean>> save(U aggregateId, T event);

  Mono<Optional<Boolean>> save(T event);

  Mono<Optional<Boolean>> save(U aggregateId, List<T> events, int version);

  Mono<Optional<Boolean>> save(U aggregateId, T event, int version);

  Mono<Optional<Boolean>> save(T event, int version);

  Mono<Integer> getVersion(U aggregateId);

  Mono<Long> countEvents(UUID aggregateId);
}
