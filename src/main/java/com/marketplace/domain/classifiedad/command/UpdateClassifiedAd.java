package com.marketplace.domain.classifiedad.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.command.Command;
import com.marketplace.domain.classifiedad.ClassifiedAdState;
import java.util.List;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableUpdateClassifiedAd.class)
@JsonDeserialize(as = ImmutableUpdateClassifiedAd.class)
public interface UpdateClassifiedAd extends Command {

  @JsonProperty("classified_ad_id")
  UUID getClassifiedAdId();

  Optional<String> getText();

  Optional<String> getTitle();

  Optional<PriceDto> getPrice();

  @JsonProperty("owner")
  Optional<UUID> getOwnerId();

  @JsonProperty("approver")
  Optional<UUID> getApprovedBy();

  Optional<List<PictureDto>> getPictures();

  Optional<ClassifiedAdState> getState();


  @Value.Immutable
  interface PictureDto {

    UUID getId();

    int getOrder();

    String getUri();

    int getWidth();

    int getHeight();
  }


  @Value.Immutable
  interface PriceDto {

    String getCurrencyCode();

    BigDecimal getAmount();
  }
}
