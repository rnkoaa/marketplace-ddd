package com.marketplace.eventstore.mongodb;

import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.eq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.eventstore.framework.event.Event;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.MongoCollection;
import java.util.List;
import java.util.UUID;
import org.bson.Document;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public class MongoEventStoreRepositoryImpl implements MongoEventStoreRepository {

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
  public Event save(UUID aggregateId, Event event) {
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
  public Event save(UUID aggregateId, Event event, int version) {
    //    ImmutableMongoEventEntity eventEntity =
    //        (ImmutableMongoEventEntity) fromEvent(event, aggregateId, version);
    //    Publisher<WriteResult> insert = mongoEventEntityRepository.insert(eventEntity);
    //    WriteResult writeResult = Mono.from(insert).block();
    //
    //    if (writeResult != null) {
    //      return event;
    //    }

    return null;
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

  private String serialize(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      return "";
    }
  }
}
