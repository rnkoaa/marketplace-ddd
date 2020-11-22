package com.marketplace.domain.classifiedad.controller;

import lombok.Data;

import java.util.UUID;

@Data
public class ResizeClassifiedAdPicture {
    private UUID id;
    private UUID classifiedAdId;
    private String uri;
    private int height;
    private int width;
}
