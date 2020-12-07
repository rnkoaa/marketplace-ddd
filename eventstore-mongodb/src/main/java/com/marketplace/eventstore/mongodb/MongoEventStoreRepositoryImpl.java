package com.marketplace.eventstore.mongodb;

import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.eventstore.framework.event.Event;
import com.marketplace.eventstore.framework.event.ImmutableTypedEvent;
import com.marketplace.eventstore.framework.event.TypedEvent;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.Success;
import io.vavr.control.Try;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MongoEventStoreRepositoryImpl implements MongoEventStoreRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(MongoEventStoreRepositoryImpl.class);
  public static final String COL_AGGREGATE_ID = "aggregateId";
  public static final String COL_ID = "_id";
  public static final String COL_VERSION = "version";
  private final EventClassCache eventClassCache = EventClassCache.getInstance();

  private final ObjectMapper objectMapper;
  private final MongoCollection<MongoEventEntity> eventCollection;

  public MongoEventStoreRepositoryImpl(
      ObjectMapper objectMapper, MongoCollection<MongoEventEntity> eventCollection) {
    this.objectMapper = objectMapper;
    this.eventCollection = eventCollection;
  }

  @Override
  public Mono<List<Event>> load(UUID aggregateId, int fromVersion) {
    // {"$and": [{"aggregateId": {"$eq": UUID('246219a4-4266-440b-964e-f292baadf133')} }, {"version": {"$gte": 2}}]}
//    FindPublisher<MongoEventEntity> findPublisher = eventCollection.find(Filters.and(
//        eq(COL_AGGREGATE_ID, aggregateId),
//        gte(COL_VERSION, fromVersion)
//    ));
    return find(Filters.and(
        eq(COL_AGGREGATE_ID, aggregateId),
        gte(COL_VERSION, fromVersion)
    ));
  }

  private Mono<List<Event>> find(Bson filter) {
    FindPublisher<MongoEventEntity> publisher = eventCollection.find(filter);
    return Flux.from(publisher).collect(Collectors.toList())
        .map(this::convertToEvent);
  }

  @Override
  public Mono<List<Event>> load(UUID aggregateId) {
    return find(eq(COL_AGGREGATE_ID, aggregateId));
  }

  private List<Event> convertToEvent(List<MongoEventEntity> eventEntities) {
    return eventEntities.stream()
        .flatMap(eventEntity -> convertToEvent(eventEntity).stream())
        .collect(Collectors.toList());
  }

  private List<Event> convertToEvent(MongoEventEntity eventEntity) {
    List<TypedEvent> events = eventEntity.getEvents();
    return events.stream()
        .map(typedEvent -> Try.of(() -> Class.forName(typedEvent.getType()))
            .onFailure(ex -> LOGGER.info("error retrieving class information: {}", ex.getMessage()))
            .flatMap(c -> Try.of(() -> objectMapper.readValue(typedEvent.getEventBody(), c))
                .onFailure(ex -> LOGGER.info("error deserializing event: {}", ex.getMessage())))
            .map(e -> (Event) e))
        .filter(Try::isSuccess)
        .map(Try::get)
        .collect(Collectors.toList());
  }

  @Override
  public Mono<Optional<Boolean>> save(UUID aggregateId, Event event) {
    return save(aggregateId, event, 0);
  }

  @Override
  public Mono<Optional<Boolean>> save(UUID aggregateId, List<Event> events, int expectedVersion) {
    Mono<Integer> nextVersionPublisher = getVersion(aggregateId)
        .map(it -> it + 1);
    Integer nextVersion = nextVersionPublisher.block();

//    // Concurrency check.
    if ((expectedVersion == 0) || (nextVersion != null && nextVersion == expectedVersion)) {
      MongoEventEntity mongoEventEntity = create(aggregateId, events, expectedVersion);
      Publisher<Success> insertOne = eventCollection.insertOne(mongoEventEntity);
      return Mono.from(insertOne)
          .map(success -> Optional.of(success == Success.SUCCESS));
    }

    LOGGER.info("expected version did not match current version: expected version: {}, current version: {}",
        expectedVersion, nextVersion);
    return Mono.just(Optional.of(false));
  }

  @Override
  public Mono<Optional<Boolean>> save(UUID aggregateId, Event event, int expectedVersion) {
    Mono<Integer> nextVersionPublisher = getVersion(aggregateId)
        .map(it -> it + 1);
    Integer nextVersion = nextVersionPublisher.block();

//    // Concurrency check.
    if ((expectedVersion == 0) || (nextVersion != null && nextVersion == expectedVersion)) {
      Try<MongoEventEntity> mongoEventEntities = create(aggregateId, event, expectedVersion);
      MongoEventEntity mongoEventEntity = mongoEventEntities
          .getOrElseThrow(() -> new EventPersistenceException("failed to convert event to be persisted"));

      Publisher<Success> insertOne = eventCollection.insertOne(mongoEventEntity);
      return Mono.from(insertOne)
          .map(success -> Optional.of(success == Success.SUCCESS));
    }
    LOGGER.info("expected version did not match current version: expected version: {}, current version: {}",
        expectedVersion, nextVersion);
    return Mono.just(Optional.of(false));
  }

  private Try<MongoEventEntity> create(UUID aggregateId, Event event, int expectedVersion) {
    Try<TypedEvent> typedEvent = fromEvent(event);
    return typedEvent.map(e -> ImmutableMongoEventEntity.builder()
        .id(event.getId())
        .aggregateId(aggregateId)
        .version(expectedVersion)
        .createdAt(event.createdAt())
        .addEvents(e)
        .streamName("ClassifiedAd:" + aggregateId.toString())
        .build());
  }

  private MongoEventEntity create(UUID aggregateId, List<Event> events, int expectedVersion) {
    List<TypedEvent> typedEvents = events.stream().map(this::fromEvent)
        .filter(Try::isSuccess)
        .map(Try::get)
        .collect(Collectors.toList());

    return ImmutableMongoEventEntity.builder()
        .id(events.get(0).getId())
        .aggregateId(aggregateId)
        .version(expectedVersion)
        .createdAt(events.get(0).createdAt())
        .addAllEvents(typedEvents)
        .streamName("ClassifiedAd:" + aggregateId.toString())
        .build();
  }

  private Try<TypedEvent> fromEvent(Event event) {
    Try<String> trySerialize = serialize(event);
    return trySerialize.map(eventBody -> ImmutableTypedEvent.builder()
        .type(event.getClass().getCanonicalName())
        .sequenceId(0)
        .eventBody(eventBody)
        .build());
  }

  @Override
  public Mono<Integer> getVersion(UUID aggregateId) {
    Publisher<Document> aggregateResultPublisher = eventCollection.aggregate(
        List.of(
            match(Filters.eq(COL_AGGREGATE_ID, aggregateId)),
            group(null, Accumulators.max(COL_VERSION, "$version"))
        ),
        Document.class);
    return Mono.from(aggregateResultPublisher)
        .map(it -> it.getInteger(COL_VERSION))
        .switchIfEmpty(Mono.just(0));
  }

  @Override
  public Mono<Long> countEvents(UUID aggregateId) {
    Publisher<Long> countPublisher = eventCollection.countDocuments(eq(COL_AGGREGATE_ID, aggregateId));
    return Mono.from(countPublisher)
        .switchIfEmpty(Mono.just(0L));
  }

  private MongoEventEntity fromEvent(Event event, UUID aggregateId, int version) {

    return ImmutableMongoEventEntity.builder()
        .aggregateId(aggregateId)
        .createdAt(event.createdAt())
        .streamName(event.aggregateName())
        //        .eventType(event.getClass().getCanonicalName())
        //        .eventBody(serialize(event))
        .id(event.getId())
        .version(version)
        .build();
  }

  private Try<String> serialize(Object object) {
    return Try.of(() -> objectMapper.writeValueAsString(object));
  }
}
