package com.marketplace.client.model.classifiedad;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableUpdateClassifiedAdTextRequest.class)
public interface UpdateClassifiedAdTextRequest {

    String getText();
}
