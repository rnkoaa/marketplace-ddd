package com.marketplace.domain.classifiedad.controller;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableAddPictureResponse.class)
@JsonSerialize(as = ImmutableAddPictureResponse.class)
public interface AddPictureResponse {

  Optional<UUID> getClassifiedAdId();

  Optional<UUID> getId();

  boolean status();

  Optional<String> getMessage();
}
