package com.marketplace.client.userprofile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableUpdateUserProfileDisplayNameRequest.class)
public interface UpdateUserProfileDisplayNameRequest {

    @JsonProperty("user_id")
    UUID getUserId();

    @JsonProperty("display_name")
    String getDisplayName();

}
