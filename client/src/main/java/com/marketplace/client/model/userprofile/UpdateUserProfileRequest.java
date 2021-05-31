package com.marketplace.client.model.userprofile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableUpdateUserProfileRequest.class)
@JsonSerialize(as = ImmutableUpdateUserProfileRequest.class)
public interface UpdateUserProfileRequest {

    @JsonProperty("user_id")
    UUID getUserId();

    @JsonProperty("first_name")
    Optional<String> getFirstName();

    @JsonProperty("last_name")
    Optional<String> getLastName();

    @JsonProperty("middle_name")
    Optional<String> getMiddleName();

    @JsonProperty("display_name")
    Optional<String> getDisplayName();

    @JsonProperty("photo_url")
    Optional<String> getPhotoUrl();
}
