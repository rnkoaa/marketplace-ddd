package com.marketplace.domain.classifiedad.controller;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateClassifiedAdResponse {
    private UUID ownerId;
    private UUID id;
}
