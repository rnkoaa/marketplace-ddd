package com.marketplace.client.model.classifiedad;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableUpdateClassifiedAdPriceRequest.class)
public interface UpdateClassifiedAdPriceRequest {

    Price getPrice();
}
