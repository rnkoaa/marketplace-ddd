package com.marketplace.eventstore.mongodb

import com.marketplace.eventstore.framework.event.Event
import com.marketplace.eventstore.test.data.TestMongoEvents
import com.mongodb.client.result.DeleteResult
import com.mongodb.reactivestreams.client.Success
import org.bson.Document
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Shared

import static com.marketplace.eventstore.test.data.TestEvents.aggregateEvents
import static com.marketplace.eventstore.test.data.TestEvents.aggregateId
import static com.marketplace.eventstore.test.data.TestEvents.testCreatedEvent
import static com.marketplace.eventstore.test.data.TestEvents.testTextUpdatedEvent
import static com.marketplace.eventstore.test.data.TestEvents.testTextUpdatedEvent2
import static com.marketplace.eventstore.test.data.TestEvents.testTitleUpdatedEvent2

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
        Mono<Optional<Boolean>> savedPublisher = eventStoreRepository.save(testCreatedEvent.aggregateId,
                testCreatedEvent, 0)

        then:
        StepVerifier.create(savedPublisher)
                .assertNext {
                    assert it.isPresent()
                    assert it.get()
                }
                .expectComplete()
                .verify()
        when:
        Mono<Long> docCount = eventStoreRepository.countEvents(testCreatedEvent.aggregateId)

        then:
        StepVerifier.create(docCount)
                .assertNext {
                    assert it == 1
                }
                .expectComplete()
                .verify()
    }

    void "an event can be saved with the aggregateId if the event has the aggregateId"() {
        when:
        Mono<Optional<Boolean>> savedPublisher = eventStoreRepository.save(testCreatedEvent)

        then:
        StepVerifier.create(savedPublisher)
                .assertNext {
                    assert it.isPresent()
                    assert it.get()
                }
                .expectComplete()
                .verify()
        when:
        Mono<Long> docCount = eventStoreRepository.countEvents(testCreatedEvent.aggregateId)

        then:
        StepVerifier.create(docCount)
                .assertNext {
                    assert it == 1
                }
                .expectComplete()
                .verify()
    }

    void "invalid expected version for subsequent event of aggregate cannot be saved"() {
        when:
        Mono<Optional<Boolean>> savedPublisher = eventStoreRepository.save(testCreatedEvent.aggregateId,
                testCreatedEvent, 0)

        then:
        StepVerifier.create(savedPublisher)
                .assertNext {
                    assert it.isPresent()
                    assert it.get()
                }
                .expectComplete()
                .verify()

        when:
        Mono<Optional<Boolean>> secondSavedPublisher = eventStoreRepository.save(testTextUpdatedEvent
                .aggregateId,
                testTextUpdatedEvent, 2)

        then:
        StepVerifier.create(secondSavedPublisher)
                .assertNext {
                    assert it.isPresent()
                    assert !it.get()
                }
                .expectComplete()
                .verify()

        when:
        Mono<Long> docCount = eventStoreRepository.countEvents(testCreatedEvent.aggregateId)

        then:
        StepVerifier.create(docCount)
                .assertNext {
                    assert it == 1
                }
                .expectComplete()
                .verify()
    }

    void "subsequent event of aggregate can be saved"() {
        when:
        Mono<Optional<Boolean>> savedPublisher = eventStoreRepository.save(testCreatedEvent.aggregateId,
                testCreatedEvent, 0)

        then:
        StepVerifier.create(savedPublisher)
                .assertNext {
                    assert it.isPresent()
                    assert it.get()
                }
                .expectComplete()
                .verify()

        when:
        Mono<Optional<Boolean>> secondSavedPublisher = eventStoreRepository.save(testTextUpdatedEvent
                .aggregateId,
                testTextUpdatedEvent, 1)

        then:
        StepVerifier.create(secondSavedPublisher)
                .assertNext {
                    assert it.isPresent()
                    assert it.get()
                }
                .expectComplete()
                .verify()

        when:
        Mono<Long> docCount = eventStoreRepository.countEvents(testCreatedEvent.aggregateId)

        then:
        StepVerifier.create(docCount)
                .assertNext {
                    assert it == 2
                }
                .expectComplete()
                .verify()
    }

    void "the maximum version of the aggregate can be retrieved"() {
        given:
        UUID testAggregateId = testCreatedEvent.aggregateId

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

    void "persisted events can be loaded via the aggregateId"() {
        when:
        Mono<Optional<Boolean>> savedPublisher = eventStoreRepository.save(testCreatedEvent.aggregateId,
                aggregateEvents, 0)

        // block to execute save
        savedPublisher.block()

        Mono<List<Event>> docCount = eventStoreRepository.load(testCreatedEvent.aggregateId)

        then:
        StepVerifier.create(docCount)
                .expectNextMatches {
                    return it.size() == 3 && it.every {
                        it.aggregateId == testCreatedEvent.aggregateId
//                                &&
//                                it.aggregateName().contains(testCreatedEvent.aggregateId.toString()
                    }
                }
                .expectComplete()
                .verify()
    }

    void "different persisted events array sizes can be loaded via the aggregateId"() {
        given:
        UUID testAggregateId = testCreatedEvent.aggregateId
        when:
        eventStoreRepository.save(testAggregateId, aggregateEvents, 0)
                .then(eventStoreRepository.save(testAggregateId, testTitleUpdatedEvent2, 1))
                .block()

        Optional<Boolean> results = eventStoreRepository.save(testAggregateId, testTextUpdatedEvent2, 2)
                .block()

        then:
        results.isPresent()

        when:
        Mono<List<Event>> loadedEvents = eventStoreRepository.load(testCreatedEvent.aggregateId)

        then:
        StepVerifier.create(loadedEvents)
                .expectNextMatches {
                    return it.size() == 5 && it.every {
                        it.aggregateId == testAggregateId
                    }
                }
                .expectComplete()
                .verify()
    }

    void "events can be loaded from a specific version"() {
        given:
        UUID testAggregateId = aggregateId
        eventStoreRepository.save(testAggregateId, aggregateEvents, 0)
                .then(eventStoreRepository.save(testAggregateId, testTitleUpdatedEvent2, 1))
                .block()

        Optional<Boolean> results = eventStoreRepository.save(testAggregateId, testTextUpdatedEvent2, 2)
                .block()

        expect:
        results.isPresent()

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
        Mono<List<Event>> versionPublisher = eventStoreRepository.load(testAggregateId, 1)

        then:
        StepVerifier.create(versionPublisher)
                .assertNext {
                    assert it.size() == 2
                }
                .expectComplete()
                .verify()
    }

    void "an array of events can be persisted successfully if the versions are correct"() {
        when:
        Mono<Optional<Boolean>> savedPublisher = eventStoreRepository.save(testCreatedEvent.aggregateId,
                aggregateEvents, 0)

        then:
        StepVerifier.create(savedPublisher)
                .assertNext {
                    assert it.isPresent()
                    assert it.get()
                }
                .expectComplete()
                .verify()

        when:
        Mono<Long> docCount = eventStoreRepository.countEvents(testCreatedEvent.aggregateId)

        then:
        StepVerifier.create(docCount)
                .assertNext {
                    assert it == 1
                }
                .expectComplete()
                .verify()
    }

}
