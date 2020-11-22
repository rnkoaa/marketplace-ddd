package com.marketplace.dagger;

import dagger.Module;
import dagger.Provides;

@Module
public class CoffeeMakerModule {

    @Provides
    static Heater provideHeater() {
        return new Heater() {
        };
    }
}
