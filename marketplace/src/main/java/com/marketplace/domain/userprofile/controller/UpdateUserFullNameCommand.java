package com.marketplace.domain.userprofile.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.domain.userprofile.FullName;

import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableUpdateUserFullNameCommand.class)
@JsonSerialize(as = ImmutableUpdateUserFullNameCommand.class)
public abstract class UpdateUserFullNameCommand {

    @JsonProperty("user_id")
    abstract UUID getUserId();

    @JsonProperty("first_name")
    abstract String getFirstName();

    @JsonProperty("last_name")
    abstract String getLastName();

    @JsonProperty("middle_name")
    abstract Optional<String> getMiddleName();

    @JsonIgnore
    public FullName fullName() {
        return new FullName(
            getFirstName(),
            getMiddleName().orElse(""),
            getLastName()
        );
    }
}
