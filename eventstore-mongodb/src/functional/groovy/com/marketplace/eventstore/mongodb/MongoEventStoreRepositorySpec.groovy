package com.marketplace.eventstore.mongodb

import com.marketplace.eventstore.test.data.TestEvents
import com.marketplace.eventstore.test.data.TestMongoEvents
import com.mongodb.client.model.Filters
import com.mongodb.reactivestreams.client.Success
import org.bson.Document
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class MongoEventStoreRepositorySpec extends BaseMongoContainerSpec {

    def setup() {
        println "MongoEventStoreRepositorySpec setup"
    }

    def cleanup() {
        // cleanup collection after each test
        eventCollection.deleteMany(new Document())
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
        Mono.just(publisher).block()

        Publisher<MongoEventEntity> foundEventEntity = eventCollection
                .find(Filters.eq("_id", testId))

        then:
        StepVerifier.create(foundEventEntity)
                .assertNext {
                    it.id == testId && it.aggregateId == TestEvents.aggregateId && it.events.size() == 1
                }
                .expectComplete()
                .verify()
    }
}
