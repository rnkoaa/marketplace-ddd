package com.marketplace.domain.userprofile.controller.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableLoadUserProfileCommand.class)
@JsonSerialize(as = ImmutableLoadUserProfileCommand.class)
public interface LoadUserProfileCommand {

    @JsonProperty("user_id")
    UUID getUserId();

}
