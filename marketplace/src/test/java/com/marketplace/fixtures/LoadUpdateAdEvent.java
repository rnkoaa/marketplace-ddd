package com.marketplace.fixtures;

import com.marketplace.context.ObjectMapperModule;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd;

import java.io.IOException;

public class LoadUpdateAdEvent {
    public static UpdateClassifiedAd load() throws IOException {
        var resourceAsStream = LoadUpdateAdEvent.class.getClassLoader().getResourceAsStream("fixtures/update_ad.json");
        return ObjectMapperModule.provideObjectMapper().readValue(resourceAsStream, UpdateClassifiedAd.class);
    }
}
