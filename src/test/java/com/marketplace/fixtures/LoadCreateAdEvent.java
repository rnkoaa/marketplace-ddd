package com.marketplace.fixtures;

import com.marketplace.domain.classifiedad.command.CreateClassifiedAd;
import com.marketplace.context.ObjectMapperModule;

import java.io.IOException;

public class LoadCreateAdEvent {


    public static CreateClassifiedAd load() throws IOException {
        var resourceAsStream = LoadCreateAdEvent.class.getClassLoader().getResourceAsStream("fixtures/create_ad.json");
        return ObjectMapperModule.provideObjectMapper().readValue(resourceAsStream, CreateClassifiedAd.class);
    }

    public static CreateClassifiedAd loadCreateAdDto() throws IOException {
        var resourceAsStream = LoadCreateAdEvent.class.getClassLoader().getResourceAsStream("fixtures/create_ad.json");
        return ObjectMapperModule.provideObjectMapper().readValue(resourceAsStream, CreateClassifiedAd.class);
    }
}
