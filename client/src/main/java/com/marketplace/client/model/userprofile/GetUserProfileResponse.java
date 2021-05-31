package com.marketplace.client.model.userprofile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableGetUserProfileResponse.class)
public interface GetUserProfileResponse {

    @JsonProperty("user_id")
    UUID getUserId();
    
    @JsonProperty("first_name")
    String getFirstName();

    @JsonProperty("last_name")
    String getLastName();

    @JsonProperty("middle_name")
    Optional<String> getMiddleName();

    @JsonProperty("display_name")
    String getDisplayName();

    @JsonProperty("photo_url")
    Optional<String> getPhotoUrl();
}
