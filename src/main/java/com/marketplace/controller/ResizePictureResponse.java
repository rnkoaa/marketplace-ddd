package com.marketplace.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResizePictureResponse {
    UUID id;
    UUID classifiedAdId;

    boolean status;
    String message;
}
