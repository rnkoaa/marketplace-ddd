package com.marketplace.client.model.userprofile;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableUpdateUserProfileResponse.class)
public interface UpdateUserProfileResponse {

    Optional<UUID> getUserId();

    Optional<String> getMessage();

    Optional<String> getStatus();
}
