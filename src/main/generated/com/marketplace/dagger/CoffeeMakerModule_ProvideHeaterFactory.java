package com.marketplace.dagger;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.processing.Generated;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class CoffeeMakerModule_ProvideHeaterFactory implements Factory<Heater> {
  @Override
  public Heater get() {
    return provideHeater();
  }

  public static CoffeeMakerModule_ProvideHeaterFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static Heater provideHeater() {
    return Preconditions.checkNotNull(CoffeeMakerModule.provideHeater(), "Cannot return null from a non-@Nullable @Provides method");
  }

  private static final class InstanceHolder {
    private static final CoffeeMakerModule_ProvideHeaterFactory INSTANCE = new CoffeeMakerModule_ProvideHeaterFactory();
  }
}
