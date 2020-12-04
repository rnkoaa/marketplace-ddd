package com.marketplace.eventstore.mongodb;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;

abstract class BaseMongoContainerSpec extends AbstractContainerInitializerSpec {
    MongoCollection<MongoEventEntity> eventCollection

    def setup() {
        CodecRegistry registry = provideCodecRegistry();
        MongoDatabase db =
                mongoClient.getDatabase(mongoConfig.getDatabase()).withCodecRegistry(registry);
        eventCollection = db.getCollection("event", MongoEventEntity.class);
    }


}
