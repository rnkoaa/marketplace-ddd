package com.marketplace.domain.classifiedad.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.marketplace.domain.classifiedad.ClassifiedAdState;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PictureDto;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PriceDto;
import java.util.List;
import java.util.Optional;
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

    Optional<String> getTitle();

    Optional<String> getText();

    Optional<PriceDto> getPrice();

    Optional<UUID> getApprover();

    ClassifiedAdState getState();

    List<PictureDto> getPictures();
}
