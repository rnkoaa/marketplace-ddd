package com.marketplace.eventstore.mongodb;

import com.marketplace.eventstore.framework.OperationResult;
import com.marketplace.eventstore.framework.event.Event;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.eventstore.framework.event.EventStream;

import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import reactor.core.publisher.Mono;

public class MongoEventStoreImpl implements EventStore<Event> {

  private final MongoEventStoreRepository eventStoreRepository;

  public MongoEventStoreImpl(MongoEventStoreRepository eventStoreRepository) {
    this.eventStoreRepository = eventStoreRepository;
  }

  @Override
  public Mono<EventStream<Event>> load(String streamId) {
    var aggregateInfo = getAggregateInfo(streamId);
    if(aggregateInfo == null) {
      return Mono.empty();
    }
    Mono<List<Event>> events = eventStoreRepository.load(aggregateInfo.aggregateId);
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

  /**
   * @param streamId format (AggregateName:AggregateId)
   * @return
   */
  private AggregateInfo getAggregateInfo(@NotNull String streamId) {
    String[] split = streamId.split(":");
    if (split.length != 2) {
      return null;
    }

    return new AggregateInfo(split[0], UUID.fromString(split[1]));
  }

  static record AggregateInfo(String aggregateName, UUID aggregateId) {

  }
}
