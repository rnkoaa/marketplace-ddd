package com.marketplace.domain.repository;

import com.marketplace.common.config.MongoConfig;
import com.marketplace.mongo.entity.MongoEntity;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import java.util.stream.Collectors;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.inject.Inject;
import java.util.*;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.mongodb.client.model.Filters.eq;

public class MongoTemplate {

  private final MongoDatabase mongoDatabase;

  @Inject
  public MongoTemplate(MongoConfig mongoConfig, MongoClient mongoClient) {
    this.mongoDatabase = mongoClient.getDatabase(mongoConfig.getDatabase());
  }

  public <T extends MongoEntity, U> Mono<T> add(T object, U id, String collectionName, Class<T> clzz) {
    Mono<Optional<T>> byIdPublisher = findById(id, collectionName, clzz);
    return byIdPublisher
        .flatMap(opt -> opt.map(exist -> update(object, id, collectionName, clzz))
            .orElseGet(() -> save(object, collectionName, clzz)))
        .switchIfEmpty(save(object, collectionName, clzz));
  }

  public <T extends MongoEntity, U> Mono<T> update(T object, U id, String collection, Class<T> clzz) {
    MongoCollection<T> mongoCollection = mongoDatabase.getCollection(collection, clzz);
    Publisher<UpdateResult> update = mongoCollection.replaceOne(eq("_id", id), object, new ReplaceOptions().upsert(true));
    return Mono.from(update)
        .flatMap(result -> {
          if (result.wasAcknowledged()) {
            return Mono.just(object);
          }
          return Mono.empty();
        });
  }

  public <T extends MongoEntity> Mono<T> save(T object, String collectionName, Class<T> clzz) {
    MongoCollection<T> collection = mongoDatabase.getCollection(collectionName, clzz);
    Publisher<InsertOneResult> insertPublisher = collection.insertOne(object);
    return Mono.from(insertPublisher)
        .flatMap(result -> {
          if (result.wasAcknowledged()) {
            return Mono.just(object);
          }
          return Mono.empty();
        });
  }

  public <T extends MongoEntity, U> Mono<Optional<T>> findById(
      U id, String collectionName, Class<T> clzz) {
    MongoCollection<T> collection = mongoDatabase.getCollection(collectionName, clzz);
    Publisher<T> result = collection.find(eq("_id", id), clzz).first();
    return Mono.from(result)
        .map(Optional::ofNullable)
        .switchIfEmpty(Mono.empty());
  }

  public <T extends MongoEntity> Mono<List<T>> findByQuery(
      String collectionName, Bson filter, Class<T> clzz) {
    MongoCollection<T> collection = mongoDatabase.getCollection(collectionName, clzz);
    FindPublisher<T> publisher = collection.find(filter, clzz);
    return Flux.from(publisher).collect(Collectors.toList());
  }

  public <T extends MongoEntity> Mono<List<T>> findAll(String collectionName, Class<T> clzz) {
    MongoCollection<T> collection = mongoDatabase.getCollection(collectionName, clzz);
    FindPublisher<T> publisher = collection.find();
    return Flux.from(publisher).collect(Collectors.toList());
  }

  public <T extends MongoEntity> Mono<DeleteResult> deleteAll(String collectionName, Class<T> clzz) {
    MongoCollection<T> collection = mongoDatabase.getCollection(collectionName, clzz);
    Publisher<DeleteResult> deleteResultPublisher = collection.deleteMany(new Document());
    return Mono.from(deleteResultPublisher);
  }
}
