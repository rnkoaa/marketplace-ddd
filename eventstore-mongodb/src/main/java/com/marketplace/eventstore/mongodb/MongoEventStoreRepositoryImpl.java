package com.marketplace.eventstore.mongodb;

import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.eq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.eventstore.framework.event.Event;
import com.marketplace.eventstore.framework.event.ImmutableTypedEvent;
import com.marketplace.eventstore.framework.event.TypedEvent;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.Success;
import io.vavr.control.Try;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class MongoEventStoreRepositoryImpl implements MongoEventStoreRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(MongoEventStoreRepositoryImpl.class);
  public static final String COL_AGGREGATE_ID = "aggregateId";
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
  public List<Event> load(UUID aggregateId, int fromVersion) {
    //    Publisher<MongoEventEntity> eventEntityPublisher =
    //        mongoEventEntityRepository
    //            .find(
    //                MongoEventEntityCriteria.mongoEventEntity
    //                    .aggregateId
    //                    .is(aggregateId)
    //                    .version
    //                    .atLeast(fromVersion))
    //            .orderBy(MongoEventEntityCriteria.mongoEventEntity.version.asc())
    //            .fetch();
    //    return Flux.from(eventEntityPublisher)
    //        .toStream()
    //        .map(this::convertToEvent)
    //        .filter(Objects::nonNull)
    //        .map(it -> (Event) it)
    //        .collect(Collectors.toList());
    return null;
  }

  @Override
  public List<Event> load(UUID aggregateId) {

    //    Publisher<MongoEventEntity> eventEntityPublisher =
    //        mongoEventEntityRepository.find(MongoEventEntityCriteria.mongoEventEntity).fetch();
    //
    //    return Flux.from(eventEntityPublisher)
    //        .toStream()
    //        .map(this::convertToEvent)
    //        .filter(Objects::nonNull)
    //        .map(it -> (Event) it)
    //        .collect(Collectors.toList());
    return null;
  }

  private Object convertToEvent(MongoEventEntity it) {
    //    String eventType = it.getEventType();
    //    String eventBody = it.getEventBody();
    //    Optional<Class<?>> optionalType = eventClassCache.get(eventType);
    //    return optionalType
    //        .map(
    //            clzz -> {
    //              try {
    //                return objectMapper.readValue(eventBody, clzz);
    //              } catch (IOException e) {
    //                e.printStackTrace();
    //              }
    //              return null;
    //            })
    //        .orElse(null);
    return null;
  }

  @Override
  public Mono<Optional<Boolean>> save(UUID aggregateId, Event event) {
    return save(aggregateId, event, 0);
  }

  @Override
  public List<Event> save(UUID aggregateId, List<Event> events, int version) {
    //    List<ImmutableMongoEventEntity> mongoEventEntities =
    //        events.stream()
    //            .map(event -> (ImmutableMongoEventEntity) fromEvent(event, aggregateId, version))
    //            .collect(Collectors.toList());
    //
    //    Publisher<WriteResult> writeResultPublisher =
    //        mongoEventEntityRepository.insertAll(mongoEventEntities);
    //
    //    WriteResult writeResult = Flux.from(writeResultPublisher).blockFirst();
    //
    //    if (writeResult != null) {
    //      return events;
    //    }

    return List.of();
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
//          .onFailure(ex -> logger.info("error while creating mongo event entity for event {} with aggregateId {}",
//              event.getId(), aggregateId))
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
