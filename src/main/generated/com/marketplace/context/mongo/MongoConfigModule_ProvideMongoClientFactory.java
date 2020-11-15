package com.marketplace.context.mongo;

import com.mongodb.client.MongoClient;
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
public final class MongoConfigModule_ProvideMongoClientFactory implements Factory<MongoClient> {
  private final Provider<MongoConfig> mongoConfigProvider;

  public MongoConfigModule_ProvideMongoClientFactory(Provider<MongoConfig> mongoConfigProvider) {
    this.mongoConfigProvider = mongoConfigProvider;
  }

  @Override
  public MongoClient get() {
    return provideMongoClient(mongoConfigProvider.get());
  }

  public static MongoConfigModule_ProvideMongoClientFactory create(
      Provider<MongoConfig> mongoConfigProvider) {
    return new MongoConfigModule_ProvideMongoClientFactory(mongoConfigProvider);
  }

  public static MongoClient provideMongoClient(MongoConfig mongoConfig) {
    return Preconditions.checkNotNull(MongoConfigModule.provideMongoClient(mongoConfig), "Cannot return null from a non-@Nullable @Provides method");
  }
}
