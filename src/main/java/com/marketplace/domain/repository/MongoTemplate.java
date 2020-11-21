package com.marketplace.domain.repository;

import com.marketplace.context.mongo.MongoConfig;
import com.marketplace.mongo.entity.MongoEntity;
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

    public <T extends MongoEntity> T save(T object, String collectionName, Class<T> clzz) {
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

    public <T extends MongoEntity, U> Optional<T> findById(U id, String collectionName, Class<T> clzz) {
        MongoCollection<T> collection = mongoDatabase.getCollection(collectionName, clzz);
        T result = collection.find(eq("_id", id), clzz).first();
        return Optional.ofNullable(result);
    }

    public <T extends MongoEntity> List<T> findAll(String collectionName, Class<T> clzz) {
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
