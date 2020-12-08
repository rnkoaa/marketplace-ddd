package com.marketplace.eventstore.mongodb;

import com.marketplace.eventstore.framework.OperationResult;
import com.marketplace.eventstore.framework.event.Event;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.eventstore.framework.event.EventStream;

import java.util.List;
import reactor.core.publisher.Mono;

public class MongoEventStoreImpl implements EventStore<Event> {

  @Override
  public Mono<EventStream<Event>> load(String streamId) {
    return null;
  }

  @Override
  public Mono<EventStream<Event>> load(String streamId, int fromVersion) {
    return null;
  }

  @Override
  public Mono<OperationResult> append(String streamId, int expectedVersion, List<Event> events) {
    return null;
  }

  @Override
  public Mono<OperationResult> append(String streamId, int expectedVersion, Event event) {
    return null;
  }

  @Override
  public Mono<Integer> size() {
    return Mono.just(0);
  }

  @Override
  public Mono<Integer> streamSize(String streamId) {
    return Mono.just(0);
  }

  @Override
  public Mono<Integer> getVersion(String streamId) {
    return Mono.just(0);
  }

  @Override
  public Mono<Integer> nextVersion(String streamId) {
    return Mono.just(0);
  }

  @Override
  public Mono<OperationResult> publish(String streamId, Event event) {
    return null;
  }

  @Override
  public Mono<OperationResult> publish(String streamId, int expectedVersion, List<Event> events) {
    return null;
  }

  @Override
  public Mono<OperationResult> publish(String streamId, int expectedVersion, Event event) {
    return null;
  }
}
