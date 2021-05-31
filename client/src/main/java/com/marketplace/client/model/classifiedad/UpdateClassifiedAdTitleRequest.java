package com.marketplace.client.model.classifiedad;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableUpdateClassifiedAdTitleRequest.class)
public interface UpdateClassifiedAdTitleRequest {

    String getTitle();
}
