package com.marketplace.controller.userprofile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateUserFullNameCommand {
    private UUID id;
    private String firstName;
    private String lastName;
    private String middleName;

    public static UpdateUserFullNameCommand from(UpdateUserFullNameDto updateUserFullName) {
        return UpdateUserFullNameCommand.builder()
                .firstName(updateUserFullName.getFirstName())
                .lastName(updateUserFullName.getLastName())
                .middleName(updateUserFullName.getMiddleName())
                .id(updateUserFullName.getUserId())
                .build();
    }
}
