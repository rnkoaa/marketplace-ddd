package com.marketplace.controller.classifiedad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAdResponse {
    private UUID ownerId;
    private UUID id;
}
