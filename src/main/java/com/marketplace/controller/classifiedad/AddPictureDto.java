package com.marketplace.controller.classifiedad;

import lombok.Data;

import java.util.UUID;

@Data
public class AddPictureDto {
    private UUID classifiedAdId;
    private String uri;
    private int height;
    private int width;
    private int order;
}
