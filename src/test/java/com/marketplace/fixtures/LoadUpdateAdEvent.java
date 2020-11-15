package com.marketplace.fixtures;

import com.marketplace.context.ObjectMapperModule;
import com.marketplace.controller.UpdateAdDto;

import java.io.IOException;

public class LoadUpdateAdEvent {
    public static UpdateAdDto load() throws IOException {
        var resourceAsStream = LoadUpdateAdEvent.class.getClassLoader().getResourceAsStream("fixtures/update_ad.json");
        return ObjectMapperModule.provideObjectMapper().readValue(resourceAsStream, UpdateAdDto.class);
    }
}
