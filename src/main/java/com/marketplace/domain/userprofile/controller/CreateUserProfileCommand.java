package com.marketplace.domain.userprofile.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.domain.userprofile.DisplayName;
import com.marketplace.domain.userprofile.FullName;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableCreateUserProfileCommand.class)
@JsonSerialize(as = ImmutableCreateUserProfileCommand.class)
public abstract class CreateUserProfileCommand {

  abstract String getFirstName();

  abstract String getLastName();

  abstract String getMiddleName();

  abstract String getDisplayName();

  abstract String getPhotoUrl();

  @JsonIgnore
  public DisplayName displayName() {
    return new DisplayName(getDisplayName());
  }

  @JsonIgnore
  public FullName fullName() {
    return new FullName(getFirstName(), getMiddleName(), getLastName());
  }
}
