package com.marketplace.eventstore.mongodb.application;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;

public class UUIDModule extends Module {

    @Override
    public String getModuleName() {
        return null;
    }

    @Override
    public Version version() {
        return null;
    }

    @Override
    public void setupModule(SetupContext context) {

    }
}
