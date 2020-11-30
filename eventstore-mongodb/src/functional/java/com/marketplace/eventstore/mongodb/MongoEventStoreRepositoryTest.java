package com.marketplace.eventstore.mongodb;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.marketplace.common.ObjectMapperBuilder;
import com.marketplace.eventstore.annotations.Event;
import com.marketplace.eventstore.framework.event.EventStore;
import com.mongodb.reactivestreams.client.MongoDatabase;
import java.util.UUID;
import org.bson.codecs.configuration.CodecRegistry;
import org.immutables.criteria.backend.Backend;
import org.immutables.criteria.mongo.MongoBackend;
import org.immutables.criteria.mongo.MongoSetup;
import org.immutables.criteria.mongo.bson4jackson.BsonModule;
import org.immutables.criteria.mongo.bson4jackson.IdAnnotationModule;
import org.immutables.criteria.mongo.bson4jackson.JacksonCodecs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

@SuppressWarnings("UnstableApiUsage")
public class MongoEventStoreRepositoryTest extends BaseMongoRepositoryTest {
  MongoEventEntityRepository mongoEventEntityRepository;
  private static final ObjectMapper objectMapper = new ObjectMapperBuilder().build();

  static MongoDatabase db;
  static Backend mongoBackend;
  MongoEventStoreRepositoryImpl eventEventStoreRepository;

  @BeforeAll
  static void initializeAll() {
    //    BaseMongoRepositoryTest.setup();

    System.out.println("MongoEventStoreRepositoryTest:setupAll");
    ObjectMapper mapper =
        new ObjectMapper()
            .findAndRegisterModules()
            // register default codecs like Jsr310, BsonValueCodec,
            .registerModule(new BsonModule())
            .registerModule(new JavaTimeModule())
            // used for Criteria.Id to '_id' attribute mapping
            .registerModule(new IdAnnotationModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    CodecRegistry registry =
        JacksonCodecs.registryFromMapper(
            mapper); // create CodecRegistry (adapter) from ObjectMapper

    db = mongoClient.getDatabase(mongoConfig.getDatabase()).withCodecRegistry(registry);
    mongoBackend = new MongoBackend(MongoSetup.of(db));
  }

  @BeforeEach
  void setupEach() {
    mongoEventEntityRepository = new MongoEventEntityRepository(mongoBackend);
    eventEventStoreRepository =
        new MongoEventStoreRepositoryImpl(objectMapper, mongoEventEntityRepository);
  }

  @AfterEach
  void cleanUp() throws InterruptedException {
    Flux.from(mongoEventEntityRepository.delete(MongoEventEntityCriteria.mongoEventEntity))
        .blockFirst();
  }

  @Test
  void findLastVersionInvalidAggregateId() {
    int version = eventEventStoreRepository.lastVersion(UUID.randomUUID());
    assertThat(version).isEqualTo(0);
  }

  @Test
  void testFindById() {
    //    UUID eventId = UUID.fromString("39aabfca-a333-448d-b644-259c550604bd");
    //    UUID aggregateId = UUID.fromString("246219a4-4266-440b-964e-f292baadf133");
    //    var testEvent = TestEvent.of(eventId, aggregateId, "test event");
    //
    //    MongoEventEntity eventEntity =
    //        ImmutableMongoEventEntity.builder()
    //            .eventBody(serialize(testEvent))
    //            .streamName("ClassifiedAd:" + aggregateId)
    //            .eventType(testEvent.getClass().getSimpleName())
    //            .aggregateId(testEvent.getAggregateId())
    //            .id(testEvent.getId())
    //            .createdAt(testEvent.createdAt())
    //            .version(0)
    //            .build();
    //
    //    Publisher<WriteResult> insertResult = mongoEventEntityRepository.insert(eventEntity);
    //    StepVerifier.create(insertResult)
    //        .assertNext(writeResult -> assertThat(writeResult).isNotNull())
    //        .verifyComplete();
    //
    //    Publisher<MongoEventEntity> found =
    //        mongoEventEntityRepository
    //            .find(MongoEventEntityCriteria.mongoEventEntity.id.is(eventId))
    //            .one();
    //
    //    StepVerifier.create(found)
    //        .consumeNextWith(
    //            mongoEventEntity -> {
    //              assertThat(mongoEventEntity).isNotNull();
    //
    // assertThat(mongoEventEntity.getAggregateId()).isNotNull().isEqualTo(aggregateId);
    //            })
    //        .verifyComplete();
  }

  @Test
  void testInsertAndFindAll() throws InterruptedException {
    //    UUID eventId = UUID.fromString("39aabfca-a333-448d-b644-259c550604bd");
    //    UUID aggregateId = UUID.fromString("246219a4-4266-440b-964e-f292baadf133");
    //    var testEvent = TestEvent.of(eventId, aggregateId, "test event");
    //
    //    MongoEventEntity eventEntity =
    //        ImmutableMongoEventEntity.builder()
    //            .eventBody(serialize(testEvent))
    //            .streamName("ClassifiedAd:" + aggregateId)
    //            .eventType(testEvent.getClass().getCanonicalName())
    //            .aggregateId(testEvent.getAggregateId())
    //            .id(testEvent.getId())
    //            .createdAt(testEvent.createdAt())
    //            .version(0)
    //            .build();
    //
    //    Publisher<WriteResult> insertResult = mongoEventEntityRepository.insert(eventEntity);
    //    StepVerifier.create(insertResult)
    //        .assertNext(writeResult -> assertThat(writeResult).isNotNull())
    //        .verifyComplete();
    //
    //    Publisher<MongoEventEntity> all = mongoEventEntityRepository.findAll().fetch();
    //
    //    StepVerifier.create(all)
    //        .consumeNextWith(
    //            mongoEventEntity -> {
    //              assertThat(mongoEventEntity).isNotNull();
    //
    //
    // assertThat(mongoEventEntity.getAggregateId()).isNotNull().isEqualTo(aggregateId);
    //            })
    //        .verifyComplete();
    //
    //    List<MongoEventEntity> flux =
    //        Flux.from(mongoEventEntityRepository.findAll().fetch()).toStream().collect(toList());
    //    assertThat(flux).hasSize(1);
    //    MongoEventEntity fluxEntity = flux.get(0);
    //    System.out.println(fluxEntity.getEventBody());
    //    System.out.println(fluxEntity.getEventType());
  }

  @Test
  void testFindByAggregateIdNotExisting() throws InterruptedException {
    //    UUID eventId = UUID.fromString("3799a0a2-6627-4797-8243-b91fe3308312");
    //
    //    Publisher<MongoEventEntity> publisher =
    //        mongoEventEntityRepository
    //            .find(MongoEventEntityCriteria.mongoEventEntity.aggregateId.is(eventId))
    //            .oneOrNone();
    //    StepVerifier.create(publisher).expectNextCount(0).verifyComplete();
  }

  @Test
  void testFindByIdNotExisting() throws InterruptedException {
    //    UUID eventId = UUID.fromString("3799a0a2-6627-4797-8243-b91fe3308312");
    //
    //    Publisher<MongoEventEntity> publisher =
    //        mongoEventEntityRepository
    //            .find(MongoEventEntityCriteria.mongoEventEntity.id.is(eventId))
    //            .oneOrNone();
    //    StepVerifier.create(publisher).expectNextCount(0).verifyComplete();
  }

  private String serialize(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      return "";
    }
  }
}
