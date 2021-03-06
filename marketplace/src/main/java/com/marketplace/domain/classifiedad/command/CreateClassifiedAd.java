package com.marketplace.domain.classifiedad.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.cqrs.command.Command;
import com.marketplace.domain.classifiedad.ClassifiedAdState;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PictureDto;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PriceDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableCreateClassifiedAd.class)
@JsonDeserialize(as = ImmutableCreateClassifiedAd.class)
public interface CreateClassifiedAd extends Command {

  @JsonProperty("classified_ad_id")
  Optional<UUID> getClassifiedAdId();

  Optional<String> getText();

  Optional<String> getTitle();

  Optional<PriceDto> getPrice();

  @JsonProperty("owner")
  UUID getOwnerId();

  @JsonProperty("approver")
  Optional<UUID> getApprovedBy();

  Optional<List<PictureDto>> getPictures();

  Optional<ClassifiedAdState> getState();


}
