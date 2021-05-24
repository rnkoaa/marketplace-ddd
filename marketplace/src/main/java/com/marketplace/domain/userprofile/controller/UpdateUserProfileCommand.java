package com.marketplace.domain.userprofile.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableUpdateUserProfileCommand.class)
@JsonSerialize(as = ImmutableUpdateUserProfileCommand.class)
public interface UpdateUserProfileCommand {

  @JsonProperty("user_id")
  UUID getUserId();

  @JsonProperty("photo_url")
  String getPhotoUrl();

}
