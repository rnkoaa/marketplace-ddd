package com.marketplace.domain.classifiedad.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.cqrs.command.Command;

import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableAddPictureToClassifiedAd.class)
@JsonDeserialize(as = ImmutableAddPictureToClassifiedAd.class)
public interface AddPictureToClassifiedAd extends Command {

  @JsonProperty("classified_ad_id")
  UUID getClassifiedAdId();

  String getUri();

  int getHeight();

  int getWidth();

  Optional<Integer> getOrder();
}
