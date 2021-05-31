package com.marketplace.domain.userprofile.controller.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableUpdateUserProfileCommand.class)
@JsonSerialize(as = ImmutableUpdateUserProfileCommand.class)
public interface UpdateUserProfileCommand {

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
