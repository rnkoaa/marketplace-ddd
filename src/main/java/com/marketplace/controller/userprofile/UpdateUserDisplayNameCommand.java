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
public class UpdateUserDisplayNameCommand {
    private UUID id;
    private String displayName;

    public static UpdateUserDisplayNameCommand from(UpdateUserDisplayNameDto dto) {
        return UpdateUserDisplayNameCommand.builder()
                .displayName(dto.getDisplayName())
                .id(dto.getUserId())
                .build();
    }
}
