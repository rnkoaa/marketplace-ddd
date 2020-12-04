package com.marketplace.eventstore.mongodb

import com.mongodb.reactivestreams.client.MongoCollection
import com.mongodb.reactivestreams.client.MongoDatabase
import org.bson.codecs.configuration.CodecRegistry

class MongoEventStoreRepositorySpec extends AbstractContainerInitializerSpec {
    MongoEventStoreRepository eventStoreRepository;

    def setup() {
        println "Setting up spec"
        CodecRegistry registry = provideCodecRegistry();
        MongoDatabase db =
                mongoClient.getDatabase(mongoConfig.getDatabase()).withCodecRegistry(registry);
        MongoCollection<MongoEventEntity> eventCollection =
                db.getCollection("event", MongoEventEntity.class);

        eventStoreRepository = new MongoEventStoreRepositoryImpl(objectMapper, eventCollection);
    }

    void "verify that eventCollection is created"() {
        expect:
        eventStoreRepository != null;
    }
}
