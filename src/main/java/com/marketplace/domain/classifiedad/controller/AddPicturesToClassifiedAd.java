package com.marketplace.domain.classifiedad.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.cqrs.command.Command;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PictureDto;
import java.util.List;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableAddPicturesToClassifiedAd.class )
@JsonSerialize(as = ImmutableAddPicturesToClassifiedAd.class )
public interface AddPicturesToClassifiedAd extends Command {
  UUID getClassifiedAdId();
  List<PictureDto> getPictures();

}
