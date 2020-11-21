package com.marketplace.controller.userprofile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserDisplayNameDto {
    private UUID userId;
    private String displayName;
}
