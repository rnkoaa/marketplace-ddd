package com.marketplace.eventstore.mongodb.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.common.ObjectMapperBuilder;
import com.marketplace.common.config.MongoConfig;
import com.marketplace.eventstore.mongodb.ImmutableMongoEventEntity;
import com.marketplace.eventstore.mongodb.MongoEventEntity;
import com.marketplace.eventstore.mongodb.MongoEventEntityRepository;
import com.marketplace.eventstore.mongodb.application.event.TestEvent;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;
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
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

@SuppressWarnings("UnstableApiUsage")
public class EventStoreApplication {
  private static final ObjectMapper objectMapper = new ObjectMapperBuilder().build();

  public static void main(String[] args) throws InterruptedException {
    //
    MongoConfig mongoConfig = new MongoConfig("localhost", "eventstore", 27017);
    MongoClient mongoClient = createClient(mongoConfig, provideCodecRegistry());
    ObjectMapper mapper =
        new ObjectMapper()
            .registerModule(
                new BsonModule()) // register default codecs like Jsr310, BsonValueCodec,
                                  // ValueCodecProvider etc.
            //              .registerModule(new GuavaModule()) // for Immutable* classes from Guava
            // (eg. ImmutableList)
            //              .registerModule(new Jdk8Module()) // used for java 8 types like Optional
            // / OptionalDouble etc.
            .registerModule(
                new IdAnnotationModule()); // used for Criteria.Id to '_id' attribute mapping

    CodecRegistry registry =
        JacksonCodecs.registryFromMapper(
            mapper); // create CodecRegistry (adapter) from ObjectMapper

    MongoDatabase db =
        mongoClient.getDatabase(mongoConfig.getDatabase()).withCodecRegistry(registry);

    Backend backend = new MongoBackend(MongoSetup.of(db));
    MongoEventEntityRepository eventEntityRepository = new MongoEventEntityRepository(backend);

    UUID eventId = UUID.fromString("39aabfca-a333-448d-b644-259c550604bd");
    UUID aggregateId = UUID.fromString("246219a4-4266-440b-964e-f292baadf133");
    var testEvent = TestEvent.of(eventId, aggregateId, "test event");

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

    Publisher<WriteResult> insertResult = eventEntityRepository.insert(eventEntity);
    insertResult.subscribe(
        new Subscriber<>() {
          @Override
          public void onSubscribe(Subscription s) {
            s.request(1);
          }

          @Override
          public void onNext(WriteResult writeResult) {
            System.out.println(writeResult);
          }

          @Override
          public void onError(Throwable t) {
            System.out.println(t.getMessage());
          }

          @Override
          public void onComplete() {
            System.out.println("complete");
          }
        });

    TimeUnit.SECONDS.sleep(2);
  }

  static MongoClient createClient(MongoConfig config, CodecRegistry codecRegistry) {
    var connectionString = new ConnectionString(config.getConnectionString());
    MongoClientSettings settings =
        MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .codecRegistry(codecRegistry)
            .build();
    return MongoClients.create(settings);
  }

  public static CodecRegistry provideCodecRegistry() {
    return CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build()),
        fromProviders(new UuidCodecProvider(UuidRepresentation.STANDARD)),
        MongoClientSettings.getDefaultCodecRegistry());
  }

  private static String serialize(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      return "";
    }
  }
}
