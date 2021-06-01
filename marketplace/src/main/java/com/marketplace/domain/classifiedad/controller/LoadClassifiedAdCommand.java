package com.marketplace.domain.classifiedad.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableLoadClassifiedAdCommand.class)
public interface LoadClassifiedAdCommand {

    public UUID getClassifiedAdId();
}
