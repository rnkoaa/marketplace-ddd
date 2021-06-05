package com.marketplace.domain.classifiedad.query;

import com.marketplace.domain.classifiedad.ClassifiedAdState;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PictureDto;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PriceDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
public interface ClassifiedAdQueryEntity {

  UUID getId();

  UUID getOwner();

  Optional<UUID> getApprover();

  Optional<String> getTitle();

  Optional<String> getText();

  Optional<PriceDto> getPrice();

  List<PictureDto> getPictures();

  ClassifiedAdState getState();

  Optional<String> getRejectionMessage();

}
