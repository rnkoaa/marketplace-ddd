package com.marketplace.controller.classifiedad;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateAdDto {
    private UUID ownerId;
    private String title;
    private String text;
}
