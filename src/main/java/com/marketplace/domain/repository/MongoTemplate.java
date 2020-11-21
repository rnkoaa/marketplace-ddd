package com.marketplace.domain.repository;

import com.marketplace.context.mongo.MongoConfig;
import com.mongodb.client.*;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class MongoTemplate {
    private final MongoDatabase mongoDatabase;

    @Inject
    public MongoTemplate(MongoConfig mongoConfig, MongoClient mongoClient) {
        this.mongoDatabase = mongoClient.getDatabase(mongoConfig.getDatabase());
    }

    public <T> T save(T object, Class<T> clzz) {
        String collectionName = clzz.getSimpleName().toLowerCase();
        MongoCollection<T> collection = mongoDatabase.getCollection(collectionName, clzz);
        try {
            InsertOneResult insertOneResult = collection.insertOne(object);
            if (insertOneResult.wasAcknowledged()) {
                String insertedId = insertOneResult.getInsertedId().toString();
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
        T result = collection.find(eq("_id", id), clzz).first();
        return Optional.ofNullable(result);
    }

    public <T> List<T> findAll(Class<T> clzz) {
        String collectionName = clzz.getSimpleName().toLowerCase();
        MongoCollection<T> collection = mongoDatabase.getCollection(collectionName, clzz);
        MongoCursor<T> iterator = collection.find().iterator();
        List<T> results = new ArrayList<>();
        while (iterator.hasNext()) {
            T next = iterator.next();
            results.add(next);
        }
        return results;
    }
}
