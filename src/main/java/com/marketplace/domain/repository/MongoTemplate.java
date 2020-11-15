package com.marketplace.domain.repository;

import com.marketplace.context.mongo.MongoConfig;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;

import javax.inject.Inject;

public class MongoTemplate {
    private final MongoClient mongoClient;
    private final MongoConfig mongoConfig;
    private final MongoDatabase mongoDatabase;

    @Inject
    public MongoTemplate(MongoConfig mongoConfig, MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        this.mongoConfig = mongoConfig;
        this.mongoDatabase = mongoClient.getDatabase(mongoConfig.getDatabase());
    }

    public <T> T save(T object, Class<T> clzz) {
        String collectionName = clzz.getSimpleName().toLowerCase();
        MongoCollection<T> collection = mongoDatabase.getCollection(collectionName, clzz);
        InsertOneResult insertOneResult = collection.insertOne(object);
        if (insertOneResult.wasAcknowledged()) {
            return object;
        }
        return null;
    }
}
