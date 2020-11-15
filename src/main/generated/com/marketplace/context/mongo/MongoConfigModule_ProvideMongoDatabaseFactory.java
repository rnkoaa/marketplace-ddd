package com.marketplace.context.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class MongoConfigModule_ProvideMongoDatabaseFactory implements Factory<MongoDatabase> {
  private final Provider<MongoClient> mongoClientProvider;

  private final Provider<MongoConfig> mongoConfigProvider;

  public MongoConfigModule_ProvideMongoDatabaseFactory(Provider<MongoClient> mongoClientProvider,
      Provider<MongoConfig> mongoConfigProvider) {
    this.mongoClientProvider = mongoClientProvider;
    this.mongoConfigProvider = mongoConfigProvider;
  }

  @Override
  public MongoDatabase get() {
    return provideMongoDatabase(mongoClientProvider.get(), mongoConfigProvider.get());
  }

  public static MongoConfigModule_ProvideMongoDatabaseFactory create(
      Provider<MongoClient> mongoClientProvider, Provider<MongoConfig> mongoConfigProvider) {
    return new MongoConfigModule_ProvideMongoDatabaseFactory(mongoClientProvider, mongoConfigProvider);
  }

  public static MongoDatabase provideMongoDatabase(MongoClient mongoClient,
      MongoConfig mongoConfig) {
    return Preconditions.checkNotNull(MongoConfigModule.provideMongoDatabase(mongoClient, mongoConfig), "Cannot return null from a non-@Nullable @Provides method");
  }
}
