package com.marketplace.domain.repository;

import com.marketplace.context.mongo.MongoConfig;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

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
        try {
            InsertOneResult insertOneResult = collection.insertOne(object);
            if (insertOneResult.wasAcknowledged()) {
                return object;
            }
        }catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public <T, U> Optional<T> findById(U id, Class<T> clzz) {
        String collectionName = clzz.getSimpleName().toLowerCase();
        MongoCollection<T> collection = mongoDatabase.getCollection(collectionName, clzz);
        T result = collection.find(eq("_id", id)).first();
        return Optional.ofNullable(result);
    }
}
