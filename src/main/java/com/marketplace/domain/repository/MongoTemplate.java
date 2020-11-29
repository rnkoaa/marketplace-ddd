package com.marketplace.domain.repository;

import com.marketplace.common.config.MongoConfig;
import com.marketplace.mongo.entity.MongoEntity;
import com.mongodb.client.*;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.inject.Inject;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class MongoTemplate {
  private final MongoDatabase mongoDatabase;

  @Inject
  public MongoTemplate(MongoConfig mongoConfig, MongoClient mongoClient) {
    this.mongoDatabase = mongoClient.getDatabase(mongoConfig.getDatabase());
  }

  public <T extends MongoEntity, U> T add(T object, U id, String collectionName, Class<T> clzz) {
    MongoCollection<T> collection = mongoDatabase.getCollection(collectionName, clzz);
    Optional<T> byId = findById(id, collectionName, clzz);
    return byId.map(
            exists -> {
              UpdateResult updateResult =
                  collection.replaceOne(eq("_id", id), object, new ReplaceOptions().upsert(true));
              if (updateResult.wasAcknowledged()) {
                return object;
              }
              return null;
            })
        .orElseGet(() -> save(object, collectionName, clzz));
  }

  public <T extends MongoEntity> T save(T object, String collectionName, Class<T> clzz) {
    MongoCollection<T> collection = mongoDatabase.getCollection(collectionName, clzz);
    try {
      InsertOneResult insertOneResult = collection.insertOne(object);
      if (insertOneResult.wasAcknowledged()) {
        return object;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      System.out.println(ex.getMessage());
    }
    return null;
  }

  public <T extends MongoEntity, U> Optional<T> findById(
      U id, String collectionName, Class<T> clzz) {
    MongoCollection<T> collection = mongoDatabase.getCollection(collectionName, clzz);
    T result = collection.find(eq("_id", id), clzz).first();
    return Optional.ofNullable(result);
  }

  public <T extends MongoEntity> List<T> findByQuery(
      String collectionName, Bson filter, Class<T> clzz) {
    MongoCollection<T> collection = mongoDatabase.getCollection(collectionName, clzz);
    MongoCursor<T> iterator = collection.find(filter, clzz).iterator();
    //    MongoCursor<T> iterator = collection.find().iterator();
    List<T> results = new ArrayList<>();
    while (iterator.hasNext()) {
      T next = iterator.next();
      results.add(next);
    }
    return results;
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

  public <T extends MongoEntity> void deleteAll(String collectionName, Class<T> clzz) {
    MongoCollection<T> collection = mongoDatabase.getCollection(collectionName, clzz);
    collection.deleteMany(new Document());
  }
}
