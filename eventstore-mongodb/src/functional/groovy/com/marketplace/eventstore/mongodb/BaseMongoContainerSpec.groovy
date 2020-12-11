package com.marketplace.eventstore.mongodb;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry
import spock.lang.Shared;

abstract class BaseMongoContainerSpec extends AbstractContainerInitializerSpec {
    @Shared
    MongoCollection<MongoEventEntity> eventCollection

    def setupSpec() {
        CodecRegistry registry = provideCodecRegistry();
        MongoDatabase db =
                mongoClient.getDatabase(mongoConfig.getDatabase()).withCodecRegistry(registry);
        eventCollection = db.getCollection("event", MongoEventEntity.class);
    }


}
