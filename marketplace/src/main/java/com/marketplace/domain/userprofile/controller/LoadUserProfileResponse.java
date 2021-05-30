package com.marketplace.domain.userprofile.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableLoadUserProfileResponse.class)
@JsonSerialize(as = ImmutableLoadUserProfileResponse.class)
public interface LoadUserProfileResponse {

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
