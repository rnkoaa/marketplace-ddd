package com.marketplace.eventstore.mongodb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.common.ObjectMapperBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MongoEventEntityRepositoryTest extends BaseMongoRepositoryTest {
//    MongoEventEntityRepository mongoEventEntityRepository;
    private static final ObjectMapper objectMapper = new ObjectMapperBuilder().build();

    @BeforeEach
    void setupEach() {

//        MongoDatabase db =
//            mongoClient.getDatabase(mongoConfig.getDatabase()).withCodecRegistry(registry);

        //    MongoDatabase eventStoreDb = mongoClient.getDatabase("eventstore");

//        Backend backend = new MongoBackend(MongoSetup.of(db));
//        mongoEventEntityRepository = new MongoEventEntityRepository(backend);
    }

    @AfterEach
    void cleanUp() throws InterruptedException {
//        mongoEventEntityRepository
//            .delete(MongoEventEntityCriteria.mongoEventEntity)
//            .subscribe(
//                new Subscriber<>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        s.request(1);
//                    }
//
//                    @Override
//                    public void onNext(WriteResult writeResult) {}
//
//                    @Override
//                    public void onError(Throwable t) {}
//
//                    @Override
//                    public void onComplete() {
//                        System.out.println("Done");
//                    }
//                });
//        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    void testFindById() {
//        UUID eventId = UUID.fromString("39aabfca-a333-448d-b644-259c550604bd");
//        UUID aggregateId = UUID.fromString("246219a4-4266-440b-964e-f292baadf133");
//        var testEvent = TestEvent.of(eventId, aggregateId, "test event");
//
//        MongoEventEntity eventEntity =
//            ImmutableMongoEventEntity.builder()
//                .eventBody(serialize(testEvent))
//                .streamName("ClassifiedAd:" + aggregateId)
//                .eventType(testEvent.getClass().getSimpleName())
//                .aggregateId(testEvent.getAggregateId())
//                .id(testEvent.getId())
//                .createdAt(testEvent.createdAt())
//                .version(0)
//                .build();
//
//        Publisher<WriteResult> insertResult = mongoEventEntityRepository.insert(eventEntity);
//        StepVerifier.create(insertResult)
//            .assertNext(writeResult -> assertThat(writeResult).isNotNull())
//            .verifyComplete();
//
//        Publisher<MongoEventEntity> found =
//            mongoEventEntityRepository
//                .find(MongoEventEntityCriteria.mongoEventEntity.id.is(eventId))
//                .one();
//
//        StepVerifier.create(found)
//            .consumeNextWith(
//                mongoEventEntity -> {
//                    assertThat(mongoEventEntity).isNotNull();
//                    AssertionsForInterfaceTypes.assertThat(mongoEventEntity.getAggregateId()).isNotNull().isEqualTo(aggregateId);
//                })
//            .verifyComplete();
    }

    @Test
    void testInsertAndFindAll() throws InterruptedException {
//        UUID eventId = UUID.fromString("39aabfca-a333-448d-b644-259c550604bd");
//        UUID aggregateId = UUID.fromString("246219a4-4266-440b-964e-f292baadf133");
//        var testEvent = TestEvent.of(eventId, aggregateId, "test event");
//
//        MongoEventEntity eventEntity =
//            ImmutableMongoEventEntity.builder()
//                .eventBody(serialize(testEvent))
//                .streamName("ClassifiedAd:" + aggregateId)
//                .eventType(testEvent.getClass().getCanonicalName())
//                .aggregateId(testEvent.getAggregateId())
//                .id(testEvent.getId())
//                .createdAt(testEvent.createdAt())
//                .version(0)
//                .build();
//
//        Publisher<WriteResult> insertResult = mongoEventEntityRepository.insert(eventEntity);
//        StepVerifier.create(insertResult)
//            .assertNext(writeResult -> assertThat(writeResult).isNotNull())
//            .verifyComplete();
//
//        Publisher<MongoEventEntity> all = mongoEventEntityRepository.findAll().fetch();
//
//        StepVerifier.create(all)
//            .consumeNextWith(
//                mongoEventEntity -> {
//                    assertThat(mongoEventEntity).isNotNull();
//
//                    AssertionsForInterfaceTypes.assertThat(mongoEventEntity.getAggregateId()).isNotNull().isEqualTo(aggregateId);
//                })
//            .verifyComplete();
//
//        List<MongoEventEntity> flux =
//            Flux.from(mongoEventEntityRepository.findAll().fetch()).toStream().collect(toList());
//        AssertionsForInterfaceTypes.assertThat(flux).hasSize(1);
//        MongoEventEntity fluxEntity = flux.get(0);
//        System.out.println(fluxEntity.getEventBody());
//        System.out.println(fluxEntity.getEventType());
    }

    @Test
    void testFindByAggregateIdNotExisting() throws InterruptedException {
//        UUID eventId = UUID.fromString("3799a0a2-6627-4797-8243-b91fe3308312");
//
//        Publisher<MongoEventEntity> publisher =
//            mongoEventEntityRepository
//                .find(MongoEventEntityCriteria.mongoEventEntity.aggregateId.is(eventId))
//                .oneOrNone();
//        StepVerifier.create(publisher).expectNextCount(0).verifyComplete();
    }

    @Test
    void testFindByIdNotExisting() throws InterruptedException {
//        UUID eventId = UUID.fromString("3799a0a2-6627-4797-8243-b91fe3308312");
//
//        Publisher<MongoEventEntity> publisher =
//            mongoEventEntityRepository
//                .find(MongoEventEntityCriteria.mongoEventEntity.id.is(eventId))
//                .oneOrNone();
//        StepVerifier.create(publisher).expectNextCount(0).verifyComplete();
    }

    private String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
