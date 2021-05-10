package com.marketplace.framework.eventsourcing;

import static com.mongodb.client.model.Filters.eq;

import com.marketplace.eventstore.framework.event.EventStreamMetadata;
import com.mongodb.DuplicateKeyException;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import java.util.UUID;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public class EventStreamMetadataRepository {

  final MongoCollection<EventStreamMetadata> collection;

  public EventStreamMetadataRepository(MongoDatabase mongoDatabase) {
    collection = mongoDatabase.getCollection("eventStreamMetadata", EventStreamMetadata.class);
  }

  public Mono<WriteResult> save(EventStreamMetadata eventStreamMetadata) {
    Publisher<InsertOneResult> publisher = collection.insertOne(eventStreamMetadata);
    return Mono.from(publisher)
        .flatMap(it -> {
          if (it.wasAcknowledged()) {
            return Mono.just(new WriteResult());
          }
          return Mono.empty();
        })
        .onErrorResume(throwable -> {
          if (throwable instanceof DuplicateKeyException) {
            return Mono.just(new WriteResult());
          }
          return Mono.empty();
        });
  }

  public Mono<EventStreamMetadata> find(UUID aggregateId) {
    Publisher<EventStreamMetadata> publisher = collection.find(eq("_id", aggregateId)).first();
    return Mono.from(publisher);
  }
}
