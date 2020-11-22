package com.marketplace.dagger;

import javax.inject.Inject;

public class CoffeeMaker {
    @Inject
    Heater heater;
    @Inject
    Pump pump;
}
