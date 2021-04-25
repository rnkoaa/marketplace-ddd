package com.marketplace.eventstore.mongodb;

import com.marketplace.cqrs.event.Event;
import com.marketplace.eventstore.framework.OperationResult;
import com.marketplace.eventstore.framework.event.EventPublisher;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.eventstore.framework.event.EventStream;

import com.marketplace.eventstore.framework.event.EventStreamImpl;
import io.vavr.control.Try;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;
import reactor.core.publisher.Mono;

public class MongoEventStoreImpl implements EventStore<Event> {

  private final MongoEventStoreRepository eventStoreRepository;
  private final EventPublisher<Event> eventPublisher;

  public MongoEventStoreImpl(MongoEventStoreRepository eventStoreRepository, EventPublisher<Event> eventPublisher) {
    this.eventStoreRepository = eventStoreRepository;
    this.eventPublisher = eventPublisher;
  }

  @Override
  public Mono<EventStream<Event>> load(String streamId) {
    var tryAggregateInfo = Try.of(() -> getAggregateInfo(streamId));
    Try<Mono<EventStream<Event>>> tryResult = tryAggregateInfo
        .map(aggregateInfo -> {
          if (aggregateInfo == null) {
            return Mono.error(new IllegalArgumentException("unable to process streamId into its parts"));
          }
          return eventStoreRepository.load(aggregateInfo.aggregateId)
              .map(events -> {
                if (events.size() > 0) {
                  // ensure the events are properly sorted.
                  events = events.stream().sorted(Comparator.comparing(Event::getVersion).thenComparing(Event::getCreatedAt))
                      .collect(Collectors.toUnmodifiableList());
                  Event lastEvent = events.get(events.size() - 1);
                  return new EventStreamImpl(streamId, lastEvent.getAggregateName(), (int) lastEvent.getVersion(), events);
                }
                return new EventStreamImpl(streamId, "", 0, List.of());
              });
        });
    if (tryResult.isSuccess()) {
      return tryResult.get();
    }
    return Mono.error(tryResult.getCause());
  }

  @Override
  public Mono<EventStream<Event>> load(String streamId, long fromVersion) {
    var tryAggregateInfo = Try.of(() -> getAggregateInfo(streamId));
    Try<Mono<EventStream<Event>>> tryResult = tryAggregateInfo
        .map(aggregateInfo -> {
          if (aggregateInfo == null) {
            return Mono.error(new IllegalArgumentException("unable to process streamId into its parts"));
          }
          return eventStoreRepository.load(aggregateInfo.aggregateId, fromVersion)
              .map(events -> {
                if (events.size() > 0) {
                  Event lastEvent = events.get(events.size() - 1);
                  return new EventStreamImpl(streamId, lastEvent.getAggregateName(), (int) lastEvent.getVersion(), events);
                }
                return new EventStreamImpl(streamId, "", 0, List.of());
              });
        });
    if (tryResult.isSuccess()) {
      return tryResult.get();
    }
    return Mono.error(tryResult.getCause());
  }

  @Override
  public Mono<OperationResult> append(String streamId, long expectedVersion, List<Event> events) {
    return parseAggregateInfo(streamId)
        .map(aggregateInfo -> eventStoreRepository.save(aggregateInfo.aggregateId, events, expectedVersion)
            .map(retVal -> {
              boolean saveStatus = retVal.orElse(false);
              if (saveStatus) {
                return new OperationResult.Success();
              }
              return new OperationResult.Failure("appending to stream failed. please try again");
            }))
        .getOrElse(Mono.error(new IllegalArgumentException("unable to parse stream id")));
  }

  @Override
  public Mono<OperationResult> append(String streamId, long expectedVersion, Event event) {
    return parseAggregateInfo(streamId)
        .map(aggregateInfo -> eventStoreRepository.save(aggregateInfo.aggregateId, event, expectedVersion)
            .map(retVal -> {
              boolean saveStatus = retVal.orElse(false);
              if (saveStatus) {
                return new OperationResult.Success();
              }
              return new OperationResult.Failure("appending to stream failed. please try again");
            }))
        .getOrElse(Mono.error(new IllegalArgumentException("unable to parse stream id")));

  }

  @Override
  public Mono<Long> size() {
    return Mono.error(new NotImplementedException("Event size not yet implemented."));
  }

  @Override
  public Mono<Long> streamSize(String streamId) {
    return parseAggregateInfo(streamId)
        .map(aggregateInfo -> eventStoreRepository.countEvents(aggregateInfo.aggregateId))
        .getOrElse(Mono.error(new IllegalArgumentException("unable to process streamId into its parts")));
  }

  @Override
  public Mono<Long> getVersion(String streamId) {
    return parseAggregateInfo(streamId)
        .map(aggregateInfo -> eventStoreRepository.getVersion(aggregateInfo.aggregateId))
        .getOrElse(Mono.error(new IllegalArgumentException("unable to process streamId into its parts")));
  }

  @Override
  public Mono<Long> nextVersion(String streamId) {
    return getVersion(streamId)
        .map(res -> res + 1);
  }

  @Override
  public Mono<OperationResult> publish(String streamId, Event event) {
    return publish(streamId, 0, event);
  }

  @Override
  public Mono<OperationResult> publish(String streamId, long expectedVersion, List<Event> events) {
    return append(streamId, 0, events)
        .doOnNext(res -> {
          if (res instanceof OperationResult.Success) {
            events.forEach(event -> eventPublisher.publish(streamId, event));
          }
        });
  }

  @Override
  public Mono<OperationResult> publish(String streamId, long expectedVersion, Event event) {
    return append(streamId, 0, event)
        .doOnNext(res -> {
          if (res instanceof OperationResult.Success) {
            eventPublisher.publish(streamId, event);
          }
        });
  }

  private Try<AggregateInfo> parseAggregateInfo(String streamId) {
    return Try.of(() -> getAggregateInfo(streamId));
  }

  /**
   * @param streamId format (AggregateName:AggregateId)
   * @return
   */
  private AggregateInfo getAggregateInfo(@NotNull String streamId) {
    String[] split = streamId.split(":");
    if (split.length != 2) {
      throw new IllegalArgumentException("Stream parts should be two");
    }

    return new AggregateInfo(split[0], UUID.fromString(split[1]));
  }

  static record AggregateInfo(String aggregateName, UUID aggregateId) {

  }
}
