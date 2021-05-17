package com.marketplace.domain.userprofile.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    abstract UUID getUserId();

    abstract String getFirstName();

    abstract String getLastName();

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
