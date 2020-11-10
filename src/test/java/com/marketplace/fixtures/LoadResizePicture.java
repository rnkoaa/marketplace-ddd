package com.marketplace.fixtures;

import com.marketplace.context.ObjectMapperModule;
import com.marketplace.controller.ResizePictureDto;

import java.io.IOException;

public class LoadResizePicture {
    public static ResizePictureDto load() throws IOException {
        var resourceAsStream = LoadCreateAdEvent.class.getClassLoader().getResourceAsStream("fixtures/resize_picture_to_ad.json");
        return ObjectMapperModule.objectMapper().readValue(resourceAsStream, ResizePictureDto.class);
    }
}