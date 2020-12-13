package com.marketplace.eventstore.mongodb


import com.marketplace.eventstore.framework.event.TypedEvent
import com.marketplace.eventstore.test.data.TestEvents
import com.marketplace.eventstore.test.data.TestMongoEvents
import com.mongodb.client.model.Accumulators
import com.mongodb.client.result.DeleteResult
import com.mongodb.reactivestreams.client.Success
import org.bson.Document
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Ignore

import static com.mongodb.client.model.Aggregates.group
import static com.mongodb.client.model.Aggregates.match
import static com.mongodb.client.model.Filters.eq

class MongoEventStoreRepositorySpec extends BaseMongoContainerSpec {

    def setup() {
        println "MongoEventStoreRepositorySpec setup"
    }

    def cleanup() {
        // cleanup collection after each test
        Publisher<DeleteResult> publisher = eventCollection.deleteMany(new Document())
        Mono.from(publisher).block()
    }

    void "verify that eventCollection is created"() {
        expect:
        eventCollection != null;
    }

    void "MongoEventEntity Can be saved  successfully"() {

        when:
        Publisher<Success> publisher = eventCollection.insertOne(TestMongoEvents.testCreatedMongoEntity)

        then:
        StepVerifier.create(publisher)
                .assertNext { it == Success.SUCCESS }
                .expectComplete()
                .verify()
    }

    void "MongoEventEntity Can be saved  and retrieved successfully"() {
        given:
        UUID testId = TestEvents.testCreatedEvent.id

        when:
        Publisher<Success> publisher = eventCollection.insertOne(TestMongoEvents.testCreatedMongoEntity)
        Mono.from(publisher).block()

        Publisher<MongoEventEntity> foundEventEntity = eventCollection
                .find(eq("_id", testId))

        then:
        StepVerifier.create(foundEventEntity)
                .assertNext {
                    assert it.id == testId &&
                            it.aggregateId == TestEvents.aggregateId &&
                            it.events.size() == 1
                }
                .expectComplete()
                .verify()
    }

    void "the maximum version of the aggregate can be retrieved"() {
        given:
        UUID testId = TestEvents.testCreatedEvent.aggregateId

        when:
        Publisher<Success> publisher = eventCollection.insertMany(TestMongoEvents.versionedEntityEvents)
        Success success = Mono.from(publisher).block()

        then:
        success == Success.SUCCESS

        when:
        Publisher<Long> documentCount = eventCollection.countDocuments()

        then:
        StepVerifier.create(documentCount)
                .assertNext {
                    assert it == 3L
                }
                .expectComplete()
                .verify()

        when:
        Publisher<Document> aggregateResultPublisher = eventCollection.aggregate(
                List.of(
                        match(eq("aggregateId", TestEvents.testCreatedEvent.aggregateId)),
                        group(null, Accumulators.max("version", '$version'))),
                Document.class);
        Mono<Document> documentMono = Mono.from(aggregateResultPublisher)

        then:
        StepVerifier.create(documentMono)
                .assertNext {
                    assert it != null
                    assert it["version"] == 2L
                }
                .expectComplete()
                .verify()
    }

    @Ignore
    void "Event can be extracted from mongoEvent Entity"() {
        given:
        UUID testId = TestEvents.testCreatedEvent.id

        when:
        Publisher<Success> publisher = eventCollection.insertOne(TestMongoEvents.testCreatedMongoEntity)
        Mono.from(publisher).block()

        Publisher<MongoEventEntity> foundEventEntity = eventCollection
                .find(eq("_id", testId))


        Mono.from(foundEventEntity)
        .map {
            List<TypedEvent> events = it.events
            events.collect {
                String className = it.type
                String eventBody = it.eventBody
                Class<?> clzz = Class.forName(className)
                Object event = deserialize(eventBody, clzz)
                return clzz.cast(event)
            }
        }

        then:
        StepVerifier.create(foundEventEntity)
                .assertNext {
                    assert it.id == testId &&
                            it.aggregateId == TestEvents.aggregateId &&
                            it.events.size() == 1
                }
                .expectComplete()
                .verify()
    }


    void "multiple MongoEventEntities Can be saved  and retrieved successfully"() {
        given:
        UUID testId = TestEvents.testCreatedEvent.id
        List<MongoEventEntity> entities = [
                TestMongoEvents.testCreatedMongoEntity,
                TestMongoEvents.testTextUpdatedMongoEntity
        ]

        when:
        Publisher<Success> publisher = eventCollection.insertMany(entities)
        Success success = Mono.from(publisher).block()

        then:
        success == Success.SUCCESS

        when:
        Publisher<MongoEventEntity> foundEventEntity = eventCollection
                .find(eq("_id", testId))

        Publisher<Long> documentCount = eventCollection.countDocuments()

        then:
        StepVerifier.create(foundEventEntity)
                .assertNext { entity ->
                    assert entity.id == testId &&
                            entity.aggregateId == TestEvents.aggregateId &&
                            entity.events.size() == 1
                }
                .expectComplete()
                .verify()

        StepVerifier.create(documentCount)
                .assertNext { it == 2 }
                .expectComplete()
                .verify()
    }
}
