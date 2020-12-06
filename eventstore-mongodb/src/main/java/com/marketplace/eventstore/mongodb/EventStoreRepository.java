package com.marketplace.eventstore.mongodb;

import java.util.List;
import java.util.UUID;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public interface EventStoreRepository<T, U> {
  List<T> load(U aggregateId, int fromVersion);

  List<T> load(U aggregateId);

  T save(U aggregateId, T event);

  List<T> save(U aggregateId, List<T> events, int version);

  T save(U aggregateId, T event, int version);

  Mono<Integer> getVersion(U aggregateId);

  Mono<Long> countEvents(UUID aggregateId);
}
