package com.marketplace.domain.classifiedad.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableResizePictureResponse.class)
@JsonDeserialize(as = ImmutableResizePictureResponse.class)
public interface ResizePictureResponse {

  UUID getId();

  UUID getClassifiedAdId();

  boolean status();

  Optional<String> getMessage();
}
