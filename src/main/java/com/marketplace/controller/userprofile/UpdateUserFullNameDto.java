package com.marketplace.controller.userprofile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserFullNameDto {
    private UUID userId;
    private String firstName;
    private String middleName;
    private String lastName;
}
