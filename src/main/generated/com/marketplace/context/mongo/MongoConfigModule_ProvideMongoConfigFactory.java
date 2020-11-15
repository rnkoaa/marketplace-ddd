package com.marketplace.context.mongo;

import com.marketplace.config.ApplicationConfig;
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
public final class MongoConfigModule_ProvideMongoConfigFactory implements Factory<MongoConfig> {
  private final Provider<ApplicationConfig> applicationConfigProvider;

  public MongoConfigModule_ProvideMongoConfigFactory(
      Provider<ApplicationConfig> applicationConfigProvider) {
    this.applicationConfigProvider = applicationConfigProvider;
  }

  @Override
  public MongoConfig get() {
    return provideMongoConfig(applicationConfigProvider.get());
  }

  public static MongoConfigModule_ProvideMongoConfigFactory create(
      Provider<ApplicationConfig> applicationConfigProvider) {
    return new MongoConfigModule_ProvideMongoConfigFactory(applicationConfigProvider);
  }

  public static MongoConfig provideMongoConfig(ApplicationConfig applicationConfig) {
    return Preconditions.checkNotNull(MongoConfigModule.provideMongoConfig(applicationConfig), "Cannot return null from a non-@Nullable @Provides method");
  }
}
