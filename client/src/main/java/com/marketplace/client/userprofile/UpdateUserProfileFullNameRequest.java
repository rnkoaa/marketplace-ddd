package com.marketplace.client.userprofile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableUpdateUserProfileFullNameRequest.class)
public interface UpdateUserProfileFullNameRequest {

    @JsonProperty("user_id")
    UUID getUserId();

    @JsonProperty("first_name")
    String getFirstName();

    @JsonProperty("last_name")
    String getLastName();

    @JsonProperty("middle_name")
    Optional<String> getMiddleName();

}
