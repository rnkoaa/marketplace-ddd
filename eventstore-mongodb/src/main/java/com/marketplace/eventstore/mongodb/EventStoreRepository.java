package com.marketplace.eventstore.mongodb;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface EventStoreRepository<T, U> {
  List<T> load(U aggregateId, int fromVersion);

  List<T> load(U aggregateId);

  Mono<Optional<Boolean>> save(U aggregateId, T event);

  List<T> save(U aggregateId, List<T> events, int version);

  Mono<Optional<Boolean>> save(U aggregateId, T event, int version);

  Mono<Integer> getVersion(U aggregateId);

  Mono<Long> countEvents(UUID aggregateId);
}
