package com.marketplace.eventstore.mongodb;

import com.marketplace.eventstore.framework.OperationResult;
import com.marketplace.eventstore.framework.event.Event;
import com.marketplace.eventstore.framework.event.EventPublisher;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.eventstore.framework.event.EventStream;

import com.marketplace.eventstore.framework.event.EventStreamImpl;
import java.util.List;
import java.util.UUID;
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
    var aggregateInfo = getAggregateInfo(streamId);
    if (aggregateInfo == null) {
      return Mono.error(new IllegalArgumentException("unable to process streamId into its parts"));
    }
    return eventStoreRepository.load(aggregateInfo.aggregateId)
        .map(events -> {
          if (events.size() > 0) {
            Event lastEvent = events.get(events.size() - 1);
            return new EventStreamImpl(streamId, "", (int) lastEvent.getVersion(), events);
          }
          return new EventStreamImpl(streamId, "", 0, List.of());
        });
  }

  @Override
  public Mono<EventStream<Event>> load(String streamId, int fromVersion) {
    var aggregateInfo = getAggregateInfo(streamId);
    if (aggregateInfo == null) {
      return Mono.error(new IllegalArgumentException("unable to process streamId into its parts"));
    }
    return eventStoreRepository.load(aggregateInfo.aggregateId, fromVersion)
        .map(events -> {
          if (events.size() > 0) {
            Event lastEvent = events.get(events.size() - 1);
            return new EventStreamImpl(streamId, "", (int) lastEvent.getVersion(), events);
          }
          return new EventStreamImpl(streamId, "", 0, List.of());
        });
  }

  @Override
  public Mono<OperationResult> append(String streamId, int expectedVersion, List<Event> events) {
    var aggregateInfo = getAggregateInfo(streamId);
    if (aggregateInfo == null) {
      return Mono.error(new IllegalArgumentException("unable to process streamId into its parts"));
    }
    return eventStoreRepository.save(aggregateInfo.aggregateId, events, expectedVersion)
        .map(retVal -> {
          if (retVal.isPresent()) {
            return retVal.map(it -> {
              if (it) {
                return new OperationResult.Success();
              }
              return new OperationResult.Failure("appending to stream failed. please try again");
            });
          }
          return new OperationResult.Failure("appending to stream failed. please try again");
        })
        .map(res -> (OperationResult) res);
  }

  @Override
  public Mono<OperationResult> append(String streamId, int expectedVersion, Event event) {
    var aggregateInfo = getAggregateInfo(streamId);
    if (aggregateInfo == null) {
      return Mono.error(new IllegalArgumentException("unable to process streamId into its parts"));
//      return Mono.empty();
    }
    return eventStoreRepository.save(aggregateInfo.aggregateId, event, expectedVersion)
        .map(retVal -> {
          if (retVal.isPresent()) {
            return retVal.map(it -> {
              if (it) {
                return new OperationResult.Success();
              }
              return new OperationResult.Failure("appending to stream failed. please try again");
            });
          }
          return new OperationResult.Failure("appending to stream failed. please try again");
        })
        .map(res -> (OperationResult) res);
  }

  @Override
  public Mono<Long> size() {
    return Mono.error(new NotImplementedException("Event size not yet implemented."));
  }

  @Override
  public Mono<Long> streamSize(String streamId) {
    var aggregateInfo = getAggregateInfo(streamId);
    if (aggregateInfo == null) {
      return Mono.error(new IllegalArgumentException("unable to process streamId into its parts"));
//      return Mono.empty();
    }
    return eventStoreRepository.countEvents(aggregateInfo.aggregateId);
  }

  @Override
  public Mono<Integer> getVersion(String streamId) {
    var aggregateInfo = getAggregateInfo(streamId);
    if (aggregateInfo == null) {
      return Mono.error(new IllegalArgumentException("unable to process streamId into its parts"));
    }

    return eventStoreRepository.getVersion(aggregateInfo.aggregateId);
  }

  @Override
  public Mono<Integer> nextVersion(String streamId) {
    return getVersion(streamId)
        .map(res -> res + 1);
  }

  @Override
  public Mono<OperationResult> publish(String streamId, Event event) {
    return publish(streamId, 0, event);
  }

  @Override
  public Mono<OperationResult> publish(String streamId, int expectedVersion, List<Event> events) {
    var aggregateInfo = getAggregateInfo(streamId);
    if (aggregateInfo == null) {
      return Mono.error(new IllegalArgumentException("unable to process streamId into its parts"));
    }
    return append(streamId, 0, events)
        .doOnNext(res -> {
          if (res instanceof OperationResult.Success) {
            events.forEach(event -> eventPublisher.publish(streamId, event));
          }
        });
  }

  @Override
  public Mono<OperationResult> publish(String streamId, int expectedVersion, Event event) {
    var aggregateInfo = getAggregateInfo(streamId);
    if (aggregateInfo == null) {
      return Mono.error(new IllegalArgumentException("unable to process streamId into its parts"));
    }
    return append(streamId, 0, event)
        .doOnNext(res -> {
          if (res instanceof OperationResult.Success) {
            eventPublisher.publish(streamId, event);
          }
        });
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
