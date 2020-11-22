package com.marketplace.controller.userprofile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserProfileCommand {
    private UUID id;
    private String photoUri;

    public static UpdateUserProfileCommand from(UpdateUserProfileDto updateUserProfileDto) {
        return new UpdateUserProfileCommand(
                updateUserProfileDto.getId(),
                updateUserProfileDto.getPhotoUrl()
        );
    }
}
