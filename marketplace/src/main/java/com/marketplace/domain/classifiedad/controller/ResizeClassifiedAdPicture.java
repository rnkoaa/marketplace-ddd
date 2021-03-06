package com.marketplace.domain.classifiedad.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableResizeClassifiedAdPicture.class)
@JsonSerialize(as = ImmutableResizeClassifiedAdPicture.class)
public interface ResizeClassifiedAdPicture {

  UUID getId();

  @JsonProperty("classified_ad_id")
  UUID getClassifiedAdId();

  String getUri();

  int getHeight();

  int getWidth();
}
