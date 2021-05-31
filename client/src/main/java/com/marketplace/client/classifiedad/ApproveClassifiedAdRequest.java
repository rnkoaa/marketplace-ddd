package com.marketplace.client.classifiedad;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableApproveClassifiedAdRequest.class)
public interface ApproveClassifiedAdRequest {

    UUID getApprovedBy();

}
