package com.marketplace.domain.classifiedad.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.command.Command;
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

  Optional<UUID> getClassifiedAdId();

  Optional<String> getText();

  Optional<String> getTitle();

  Optional<PriceDto> getPrice();

  UUID getOwnerId();

  Optional<UUID> getApprovedBy();

  List<PictureDto> getPictures();

  Optional<ClassifiedAdState> getState();


}
