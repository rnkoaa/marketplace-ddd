package com.marketplace.fixtures;

import com.marketplace.domain.command.CreateClassifiedAd;
import com.marketplace.context.ObjectMapperModule;
import com.marketplace.controller.CreateAdDto;

import java.io.IOException;

public class LoadCreateAdEvent {


    public static CreateClassifiedAd load() throws IOException {
        var resourceAsStream = LoadCreateAdEvent.class.getClassLoader().getResourceAsStream("fixtures/create_ad.json");
        return ObjectMapperModule.objectMapper().readValue(resourceAsStream, CreateClassifiedAd.class);
    }

    public static CreateAdDto loadCreateAdDto() throws IOException {
        var resourceAsStream = LoadCreateAdEvent.class.getClassLoader().getResourceAsStream("fixtures/create_ad.json");
        return ObjectMapperModule.objectMapper().readValue(resourceAsStream, CreateAdDto.class);
    }
}