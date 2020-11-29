package com.marketplace.context.mongo;

import com.marketplace.common.config.MongoConfig;
import com.marketplace.config.ApplicationConfig;
import com.marketplace.context.mongo.codec.DisplayNameCodecProviderImpl;
import com.marketplace.context.mongo.codec.PriceCodecProviderImpl;
import com.marketplace.context.mongo.codec.PriceConverter;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import dagger.Module;
import dagger.Provides;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import javax.inject.Singleton;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

@Module
public class MongoConfigModule {

    @Provides
    @Singleton
    public static MongoConfig provideMongoConfig(ApplicationConfig applicationConfig) {
        return applicationConfig.getMongo();
    }

    @Provides
    @Singleton
    public static MongoClient provideMongoClient(MongoConfig mongoConfig) {
        var connectionString = new ConnectionString(mongoConfig.getConnectionString());
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistries())
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build();
        return MongoClients.create(settings);
    }

    private static CodecRegistry codecRegistries() {
        return CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()),
                fromProviders(new UuidCodecProvider(UuidRepresentation.STANDARD),
                        new PriceCodecProviderImpl(new PriceConverter()),
                        new DisplayNameCodecProviderImpl()),
                MongoClientSettings.getDefaultCodecRegistry()
        );
    }

    @Provides
    @Singleton
    public static MongoDatabase provideMongoDatabase(MongoClient mongoClient, MongoConfig mongoConfig) {
        return mongoClient.getDatabase(mongoConfig.getDatabase());
    }

}
