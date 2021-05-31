package com.marketplace.client.classifiedad;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableClassifiedAdRequest.class)
public interface ClassifiedAdRequest {

    UUID getId();

    UUID getOwnerId();

    UUID getApprovedBy();

    String getTitle();

    String getText();

    Price getPrice();

    List<Picture> getPictures();
}
