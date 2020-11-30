package com.marketplace.eventstore.mongodb;

import com.marketplace.common.config.MongoConfig;
import com.marketplace.common.test.AbstractContainerInitializer;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

public abstract class BaseMongoRepositoryTest extends AbstractContainerInitializer {
  static MongoConfig mongoConfig;
  static MongoClient mongoClient;

  @BeforeAll
  public static void setup() {
    System.out.println("BaseMongoRepositoryTest:setup");
    String hosts = mongoDBContainer.getHost();
    int port = mongoDBContainer.getMappedPort(27017);
    mongoConfig = new MongoConfig(hosts, "event_store", port);
    mongoClient = createClient(mongoConfig, provideCodecRegistry());
  }

  @AfterEach
  public void cleanup() {
    //        DeleteResult deleteResult = classifiedAdCollection.deleteMany(new Document());
    //        if (deleteResult.wasAcknowledged()) {
    //            System.out.println("delete was acknowledged.");
    //        }
    //        System.out.println("Number of records deleted: " + deleteResult.getDeletedCount());
  }

  static MongoClient createClient(MongoConfig config, CodecRegistry codecRegistry) {
    var connectionString = new ConnectionString(mongoConfig.getConnectionString());
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
}
