package com.marketplace.dagger;

import dagger.MembersInjector;
import dagger.internal.InjectedFieldSignature;
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
public final class CoffeeMaker_MembersInjector implements MembersInjector<CoffeeMaker> {
  private final Provider<Heater> heaterProvider;

  private final Provider<Pump> pumpProvider;

  public CoffeeMaker_MembersInjector(Provider<Heater> heaterProvider, Provider<Pump> pumpProvider) {
    this.heaterProvider = heaterProvider;
    this.pumpProvider = pumpProvider;
  }

  public static MembersInjector<CoffeeMaker> create(Provider<Heater> heaterProvider,
      Provider<Pump> pumpProvider) {
    return new CoffeeMaker_MembersInjector(heaterProvider, pumpProvider);
  }

  @Override
  public void injectMembers(CoffeeMaker instance) {
    injectHeater(instance, heaterProvider.get());
    injectPump(instance, pumpProvider.get());
  }

  @InjectedFieldSignature("com.marketplace.dagger.CoffeeMaker.heater")
  public static void injectHeater(CoffeeMaker instance, Heater heater) {
    instance.heater = heater;
  }

  @InjectedFieldSignature("com.marketplace.dagger.CoffeeMaker.pump")
  public static void injectPump(CoffeeMaker instance, Pump pump) {
    instance.pump = pump;
  }
}
