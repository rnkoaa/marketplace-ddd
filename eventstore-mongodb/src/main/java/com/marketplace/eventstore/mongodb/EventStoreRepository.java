package com.marketplace.eventstore.mongodb;

import java.util.List;

public interface EventStoreRepository<T, U> {
  List<T> load(U aggregateId, int fromVersion);

  List<T> load(U aggregateId);

  T save(U aggregateId, T event);

  List<T> save(U aggregateId, List<T> events, int version);

  T save(U aggregateId, T event, int version);

  int lastVersion(U aggregateId);
}
