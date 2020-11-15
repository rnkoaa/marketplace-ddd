package com.marketplace.context.server;

import com.marketplace.config.ApplicationConfig;
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
public final class SparkServerModule_ProvideServerPortFactory implements Factory<Integer> {
  private final Provider<ApplicationConfig> applicationConfigProvider;

  public SparkServerModule_ProvideServerPortFactory(
      Provider<ApplicationConfig> applicationConfigProvider) {
    this.applicationConfigProvider = applicationConfigProvider;
  }

  @Override
  public Integer get() {
    return provideServerPort(applicationConfigProvider.get());
  }

  public static SparkServerModule_ProvideServerPortFactory create(
      Provider<ApplicationConfig> applicationConfigProvider) {
    return new SparkServerModule_ProvideServerPortFactory(applicationConfigProvider);
  }

  public static int provideServerPort(ApplicationConfig applicationConfig) {
    return SparkServerModule.provideServerPort(applicationConfig);
  }
}
