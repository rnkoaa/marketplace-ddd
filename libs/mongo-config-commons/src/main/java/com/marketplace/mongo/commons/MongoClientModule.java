package com.marketplace.mongo.commons;

import com.marketplace.common.config.MongoConfig;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dagger.Module;
import dagger.Provides;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistry;

import javax.inject.Singleton;

@Module
public class MongoClientModule {
  @Provides
  @Singleton
  public static MongoClient provideMongoClient(MongoConfig mongoConfig, CodecRegistry codecRegistry) {
    var connectionString = new ConnectionString(mongoConfig.getConnectionString());
    MongoClientSettings settings =
        MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .codecRegistry(codecRegistry)
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .build();
    return MongoClients.create(settings);
  }
}
