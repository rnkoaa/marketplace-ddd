package com.marketplace.domain.userprofile.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.domain.userprofile.DisplayName;
import com.marketplace.domain.userprofile.FullName;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableCreateUserProfileCommand.class)
@JsonSerialize(as = ImmutableCreateUserProfileCommand.class)
public abstract class CreateUserProfileCommand {

    @JsonProperty("first_name")
    public abstract String getFirstName();

    @JsonProperty("last_name")
    public abstract String getLastName();

    @JsonProperty("middle_name")
    public abstract Optional<String> getMiddleName();

    @JsonProperty("display_name")
    public abstract String getDisplayName();

    @JsonProperty("photo_url")
    public abstract Optional<String> getPhotoUrl();

    @JsonIgnore
    public DisplayName displayName() {
        return new DisplayName(getDisplayName());
    }

    @JsonIgnore
    public FullName fullName() {
        return new FullName(getFirstName(),
            getMiddleName().orElse(""),
            getLastName());
    }
}
