package com.marketplace.mongo.commons;

import com.mongodb.MongoClientSettings;
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
public class MongoCodecRegistryModule {
  @Provides
  @Singleton
  public static CodecRegistry provideCodecRegistry() {
    return CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build()),
        fromProviders(new UuidCodecProvider(UuidRepresentation.STANDARD)),
        MongoClientSettings.getDefaultCodecRegistry());
  }
}
