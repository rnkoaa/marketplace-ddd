package com.marketplace.fixtures;

import com.marketplace.context.ObjectMapperModule;
import com.marketplace.domain.classifiedad.controller.AddPictureToClassifiedAd;

import java.io.IOException;

public class LoadAddPicture {
    public static AddPictureToClassifiedAd load() throws IOException {
        var resourceAsStream = LoadCreateAdEvent.class.getClassLoader().getResourceAsStream("fixtures/add_picture_to_ad.json");
        return ObjectMapperModule.provideObjectMapper().readValue(resourceAsStream, AddPictureToClassifiedAd.class);
    }
}
