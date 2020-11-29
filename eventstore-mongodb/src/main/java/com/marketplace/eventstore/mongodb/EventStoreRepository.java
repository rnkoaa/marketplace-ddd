package com.marketplace.eventstore.mongodb;

import com.marketplace.eventstore.framework.event.Event;

import java.util.List;
import java.util.UUID;

public interface EventStoreRepository<T, U> {
  List<T> load(U aggregateId, int fromVersion);

  List<T> load(U aggregateId);

  T save(U aggregateId, T event);

  List<T> save(U aggregateId, List<T> events, int version);

  T save(U aggregateId, T event, int version);

  int lastVersion(U aggregateId);
}
