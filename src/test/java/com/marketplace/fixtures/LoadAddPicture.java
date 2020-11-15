package com.marketplace.fixtures;

import com.marketplace.context.ObjectMapperModule;
import com.marketplace.controller.AddPictureDto;

import java.io.IOException;

public class LoadAddPicture {
    public static AddPictureDto load() throws IOException {
        var resourceAsStream = LoadCreateAdEvent.class.getClassLoader().getResourceAsStream("fixtures/add_picture_to_ad.json");
        return ObjectMapperModule.provideObjectMapper().readValue(resourceAsStream, AddPictureDto.class);
    }
}
