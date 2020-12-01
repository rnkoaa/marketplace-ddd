package com.marketplace.eventstore.mongodb.application;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.common.ObjectMapperBuilder;
import com.marketplace.common.config.MongoConfig;
import com.marketplace.eventstore.mongodb.ImmutableMongoEventEntity;
import com.marketplace.eventstore.mongodb.MongoEventEntity;
import com.marketplace.eventstore.mongodb.MongoEventEntityRepository;
import com.marketplace.eventstore.mongodb.application.codecs.CustomBsonModule;
import com.marketplace.eventstore.mongodb.application.codecs.ObjectMapperCodecs;
import com.marketplace.eventstore.mongodb.application.event.TestEvent;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.Success;
import java.util.UUID;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.immutables.criteria.backend.Backend;
import org.immutables.criteria.backend.WriteResult;
import org.immutables.criteria.mongo.MongoBackend;
import org.immutables.criteria.mongo.MongoSetup;
import org.immutables.criteria.mongo.bson4jackson.BsonModule;
import org.immutables.criteria.mongo.bson4jackson.IdAnnotationModule;
import org.immutables.criteria.mongo.bson4jackson.JacksonCodecs;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@SuppressWarnings("UnstableApiUsage")
public class EventStoreApplication {
  private static final ObjectMapper objectMapper = new ObjectMapperBuilder().build();

  public static void main(String[] args) throws InterruptedException {
    //
    MongoConfig mongoConfig = new MongoConfig("localhost", "eventstore", 27017);
    MongoClient mongoClient = createClient(mongoConfig, provideCodecRegistry());
    MongoDatabase db =
        mongoClient
            .getDatabase(mongoConfig.getDatabase())
            .withCodecRegistry(provideCodecRegistry());
    //
    Backend backend = new MongoBackend(MongoSetup.of(db));
    MongoEventEntityRepository eventEntityRepository = new MongoEventEntityRepository(backend);
    //
    UUID eventId = UUID.fromString("39aabfca-a333-448d-b644-259c550604bd");
    UUID aggregateId = UUID.fromString("246219a4-4266-440b-964e-f292baadf133");
    var testEvent = TestEvent.of(UUID.randomUUID(), aggregateId, "test event");
    MongoEventEntity eventEntity =
        ImmutableMongoEventEntity.builder()
            .eventBody(serialize(testEvent))
            .streamName("ClassifiedAd:" + aggregateId)
            .eventType(testEvent.getClass().getSimpleName())
            .aggregateId(testEvent.getAggregateId())
            .id(testEvent.getId())
            .createdAt(testEvent.createdAt())
            .version(0)
            .build();

    //    MongoCollection<MongoEventEntity> collection = db.getCollection("test",
    // MongoEventEntity.class);
    //    Publisher<Success> insertResult = collection.insertOne(eventEntity);
    Publisher<WriteResult> insertResult = eventEntityRepository.insert(eventEntity);
    Mono.from(insertResult).block();

    testEvent = TestEvent.of(UUID.randomUUID(), aggregateId, "test event 1");
    eventEntity =
        ImmutableMongoEventEntity.builder()
            .eventBody(serialize(testEvent))
            .streamName("ClassifiedAd:" + aggregateId)
            .eventType(testEvent.getClass().getSimpleName())
            .aggregateId(testEvent.getAggregateId())
            .id(UUID.randomUUID())
            .createdAt(testEvent.createdAt())
            .version(1)
            .build();
    insertResult = eventEntityRepository.insert(eventEntity);
    Mono.from(insertResult).block();

    testEvent = TestEvent.of(UUID.randomUUID(), aggregateId, "test event 2");
    eventEntity =
        ImmutableMongoEventEntity.builder()
            .eventBody(serialize(testEvent))
            .streamName("ClassifiedAd:" + aggregateId)
            .eventType(testEvent.getClass().getSimpleName())
            .aggregateId(testEvent.getAggregateId())
            .id(UUID.randomUUID())
            .createdAt(testEvent.createdAt())
            .version(2)
            .build();
    insertResult = eventEntityRepository.insert(eventEntity);
    Mono.from(insertResult).block();

    // db.mongoEventEntity.aggregate([{
    //  $match: { aggregateId: UUID('246219a4-4266-440b-964e-f292baadf133')}
    // },{
    //  $group: {
    //  _id: null,
    //  version: {
    //    $max: "$version"
    //  }
    // }}])


    //
    //    Mono.from(insertResult).block();
    //    insertResult.subscribe(
    //        new Subscriber<>() {
    //          @Override
    //          public void onSubscribe(Subscription s) {
    //            s.request(1);
    //          }
    //
    //          @Override
    //          public void onNext(WriteResult writeResult) {
    //            System.out.println(writeResult);
    //          }
    //
    //          @Override
    //          public void onError(Throwable t) {
    //            System.out.println(t.getMessage());
    //          }
    //
    //          @Override
    //          public void onComplete() {
    //            System.out.println("complete");
    //          }
    //        });
    //
    //    TimeUnit.SECONDS.sleep(2);
  }

  static MongoClient createClient(MongoConfig config, CodecRegistry codecRegistry) {
    var connectionString = new ConnectionString(config.getConnectionString());
    MongoClientSettings settings =
        MongoClientSettings.builder()
            //            .uuidRepresentation(UuidRepresentation.STANDARD)
            .applyConnectionString(connectionString)
            .codecRegistry(codecRegistry)
            .build();
    return MongoClients.create(settings);
  }

  public static CodecRegistry provideCodecRegistry() {
    ObjectMapper mapper =
        new ObjectMapper()
            .registerModule(new BsonModule())
            // register default codecs like Jsr310, BsonValueCodec,
            .registerModule(new CustomBsonModule())
            // ValueCodecProvider etc.
            //              .registerModule(new GuavaModule()) // for Immutable* classes from Guava
            // (eg. ImmutableList)
            //              .registerModule(new Jdk8Module()) // used for java 8 types like Optional
            // / OptionalDouble etc.
            .registerModule(new IdAnnotationModule());

    return CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        JacksonCodecs.registryFromMapper(mapper),
        fromProviders(PojoCodecProvider.builder().automatic(true).build()),
        fromProviders(new UuidCodecProvider(UuidRepresentation.STANDARD)));
  }

  private static String serialize(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      return "";
    }
  }
}
