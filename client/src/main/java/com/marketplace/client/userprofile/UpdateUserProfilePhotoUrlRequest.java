package com.marketplace.client.userprofile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableUpdateUserProfilePhotoUrlRequest.class)
public interface UpdateUserProfilePhotoUrlRequest {

    @JsonProperty("user_id")
    UUID getUserId();

    @JsonProperty("photo_url")
    String getPhotoUrl();
}
