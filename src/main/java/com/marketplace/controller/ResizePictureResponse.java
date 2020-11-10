package com.marketplace.controller;

import lombok.Data;

import java.util.UUID;

@Data
public class ResizePictureResponse {
    UUID id;
    UUID classifiedAdId;

    boolean status;
    String message;
}
