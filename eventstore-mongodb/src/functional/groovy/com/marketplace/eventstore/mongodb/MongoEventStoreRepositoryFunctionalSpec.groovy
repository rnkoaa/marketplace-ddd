package com.marketplace.eventstore.mongodb

import com.marketplace.eventstore.test.data.TestEvents
import com.marketplace.eventstore.test.data.TestMongoEvents
import com.mongodb.client.result.DeleteResult
import com.mongodb.reactivestreams.client.Success
import org.bson.Document
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Shared

class MongoEventStoreRepositoryFunctionalSpec extends BaseMongoContainerSpec {

    @Shared
    MongoEventStoreRepository eventStoreRepository

    def setupSpec() {
        eventStoreRepository = new MongoEventStoreRepositoryImpl(objectMapper, eventCollection)
    }

    def setup() {
        println "MongoEventStoreRepositorySpec setup"
    }

    def cleanup() {
        // cleanup collection after each test
        Publisher<DeleteResult> publisher = eventCollection.deleteMany(new Document())
        Mono.from(publisher).block()
    }

    void "first event of aggregate can be saved"() {
        when:
        Mono<Optional<Boolean>> savedPublisher = eventStoreRepository.save(TestEvents.testCreatedEvent.aggregateId,
                TestEvents.testCreatedEvent, 0)

        then:
        StepVerifier.create(savedPublisher)
                .assertNext {
                    assert it.isPresent()
                    assert it.get()
                }

        when:
        Mono<Long> docCount = eventStoreRepository.countEvents(TestEvents.testCreatedEvent.aggregateId)

        then:
        StepVerifier.create(docCount)
                .assertNext {
                    assert it == 1
                }
    }

    void "the maximum version of the aggregate can be retrieved"() {
        given:
        UUID testAggregateId = TestEvents.testCreatedEvent.aggregateId

        when:
        Publisher<Success> publisher = eventCollection.insertMany(TestMongoEvents.versionedEntityEvents)
        Success success = Mono.from(publisher).block()

        then:
        success == Success.SUCCESS

        when:
        Mono<Long> documentCount = eventStoreRepository.countEvents(testAggregateId)

        then:
        StepVerifier.create(documentCount)
                .assertNext {
                    assert it == 3
                }
                .expectComplete()
                .verify()

        when:
        Mono<Integer> versionPublisher = eventStoreRepository.getVersion(testAggregateId)

        then:
        StepVerifier.create(versionPublisher)
                .assertNext {
                    assert it == 2
                }
                .expectComplete()
                .verify()
    }

    void "missing aggregate will have 0 as the maximum version"() {
        given:
        UUID aggregateId = UUID.randomUUID()

        when:
        Mono<Integer> versionPublisher = eventStoreRepository.getVersion(aggregateId)

        then:
        StepVerifier.create(versionPublisher)
                .assertNext {
                    assert it == 0
                }
                .expectComplete()
                .verify()

    }

}
