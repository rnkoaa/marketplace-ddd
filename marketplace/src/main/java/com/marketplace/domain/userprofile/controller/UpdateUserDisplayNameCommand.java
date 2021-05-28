package com.marketplace.domain.userprofile.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.domain.userprofile.DisplayName;

import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableUpdateUserDisplayNameCommand.class)
@JsonSerialize(as = ImmutableUpdateUserDisplayNameCommand.class)
public abstract class UpdateUserDisplayNameCommand {

    @JsonProperty("user_id")
    public abstract UUID getUserId();

    @JsonProperty("display_name")
    public abstract String getDisplayName();

    @JsonIgnore
    public DisplayName displayName() {
        return new DisplayName(getDisplayName());
    }

}
