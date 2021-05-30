package com.marketplace.client.model.classifiedad;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutablePicture.class)
public interface Picture {

    String getId();

    int getWidth();

    int getHeight();

    String getURI();

    int getOrder();
}
