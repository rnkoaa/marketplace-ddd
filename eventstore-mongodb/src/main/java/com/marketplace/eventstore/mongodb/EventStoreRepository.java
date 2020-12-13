package com.marketplace.eventstore.mongodb;

import com.marketplace.eventstore.framework.event.Event;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface EventStoreRepository<T, U> {

  Mono<List<Event>> load(U aggregateId, long fromVersion);

  Mono<List<Event>> load(U aggregateId);

  Mono<Optional<Boolean>> save(U aggregateId, T event);

  Mono<Optional<Boolean>> save(T event);

  Mono<Optional<Boolean>> save(U aggregateId, List<T> events, long version);

  Mono<Optional<Boolean>> save(U aggregateId, T event, long version);

  Mono<Optional<Boolean>> save(T event, long version);

  Mono<Long> getVersion(U aggregateId);

  Mono<Long> countEvents(UUID aggregateId);
}
