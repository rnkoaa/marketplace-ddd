package com.marketplace.controller;

import lombok.Data;

import java.util.UUID;

@Data
public class AddPictureResponse {
    UUID id;
    UUID classifiedAdId;

    boolean status;
    String message;
}
