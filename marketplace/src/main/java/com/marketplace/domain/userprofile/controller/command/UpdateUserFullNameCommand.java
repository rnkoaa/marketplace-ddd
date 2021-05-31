package com.marketplace.domain.userprofile.controller.command;

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
    public abstract UUID getUserId();

    @JsonProperty("first_name")
    public abstract String getFirstName();

    @JsonProperty("last_name")
    public abstract String getLastName();

    @JsonProperty("middle_name")
    public abstract Optional<String> getMiddleName();

    @JsonIgnore
    public FullName fullName() {
        return new FullName(
            getFirstName(),
            getMiddleName().orElse(""),
            getLastName()
        );
    }
}
