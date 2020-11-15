package com.marketplace.dagger;

import javax.inject.Inject;

public class Thermosiphon implements Pump {
    final Heater heater;

    @Inject
    public Thermosiphon(Heater heater) {
        this.heater = heater;
    }
}
