package com.marketplace.domain.classifiedad.controller;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableUpdateClassifiedAdResponse.class)
@JsonSerialize(as = ImmutableUpdateClassifiedAdResponse.class)
public interface UpdateClassifiedAdResponse {

  UUID getOwnerId();

  UUID getId();
}
