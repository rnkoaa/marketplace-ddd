package com.marketplace.domain.repository;

import com.marketplace.context.mongo.MongoConfig;
import com.mongodb.client.MongoClient;
import dagger.internal.Factory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class MongoTemplate_Factory implements Factory<MongoTemplate> {
  private final Provider<MongoConfig> mongoConfigProvider;

  private final Provider<MongoClient> mongoClientProvider;

  public MongoTemplate_Factory(Provider<MongoConfig> mongoConfigProvider,
      Provider<MongoClient> mongoClientProvider) {
    this.mongoConfigProvider = mongoConfigProvider;
    this.mongoClientProvider = mongoClientProvider;
  }

  @Override
  public MongoTemplate get() {
    return newInstance(mongoConfigProvider.get(), mongoClientProvider.get());
  }

  public static MongoTemplate_Factory create(Provider<MongoConfig> mongoConfigProvider,
      Provider<MongoClient> mongoClientProvider) {
    return new MongoTemplate_Factory(mongoConfigProvider, mongoClientProvider);
  }

  public static MongoTemplate newInstance(MongoConfig mongoConfig, MongoClient mongoClient) {
    return new MongoTemplate(mongoConfig, mongoClient);
  }
}
