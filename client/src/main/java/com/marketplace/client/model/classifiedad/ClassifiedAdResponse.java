package com.marketplace.client.model.classifiedad;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableClassifiedAdResponse.class)
public interface ClassifiedAdResponse {

    UUID getId();

    UUID getOwnerId();

    UUID getApprover();

    String getTitle();

    String getText();

    Price getPrice();

    List<Picture> getPictures();

    ClassifiedAdState getState();

}
