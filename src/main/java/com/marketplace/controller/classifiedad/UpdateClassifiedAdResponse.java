package com.marketplace.controller.classifiedad;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateClassifiedAdResponse {
    private UUID ownerId;
    private UUID id;
}
