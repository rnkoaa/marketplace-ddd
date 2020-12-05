package com.marketplace.eventstore.mongodb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.eventstore.framework.event.Event;
import com.mongodb.reactivestreams.client.MongoCollection;
import java.util.List;
import java.util.UUID;

public class MongoEventStoreRepositoryImpl implements MongoEventStoreRepository {

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
  public int lastVersion(UUID aggregateId) {
    //    Publisher<Integer> publisher =
    //        mongoEventEntityRepository
    //            .find(MongoEventEntityCriteria.mongoEventEntity.aggregateId.is(aggregateId))
    //            .select(MongoEventEntityCriteria.mongoEventEntity.version.max())
    //            .oneOrNone();
    //
    //    return Mono.from(publisher).switchIfEmpty(Mono.just(0)).block();
    return 0;
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
