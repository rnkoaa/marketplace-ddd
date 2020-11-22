package com.marketplace.domain.userprofile.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketplace.domain.userprofile.DisplayName;
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

    @JsonIgnore
    public DisplayName displayName(){
        return new DisplayName(displayName);
    }

}
