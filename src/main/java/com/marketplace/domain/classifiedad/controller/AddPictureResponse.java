package com.marketplace.domain.classifiedad.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddPictureResponse {
    UUID id;
    UUID classifiedAdId;
    boolean status;
    String message;
}
