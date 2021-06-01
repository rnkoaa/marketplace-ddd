package com.marketplace.domain.classifiedad.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableLoadClassifiedAdResponse.class)
public interface LoadClassifiedAdResponse {

    /*
      private ClassifiedAdId id;
  private final List<Picture> pictures;
  private UserId ownerId;
  private ClassifiedAdTitle title;
  private ClassifiedAdText text;
  private Price price;
  private UserId approvedBy;
  private ClassifiedAdState state;

     */
    UUID getClassifiedAdId();

    UUID getOwner();
}
