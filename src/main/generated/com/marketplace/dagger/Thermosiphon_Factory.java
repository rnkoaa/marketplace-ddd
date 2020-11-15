package com.marketplace.dagger;

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
public final class Thermosiphon_Factory implements Factory<Thermosiphon> {
  private final Provider<Heater> heaterProvider;

  public Thermosiphon_Factory(Provider<Heater> heaterProvider) {
    this.heaterProvider = heaterProvider;
  }

  @Override
  public Thermosiphon get() {
    return newInstance(heaterProvider.get());
  }

  public static Thermosiphon_Factory create(Provider<Heater> heaterProvider) {
    return new Thermosiphon_Factory(heaterProvider);
  }

  public static Thermosiphon newInstance(Heater heater) {
    return new Thermosiphon(heater);
  }
}
