package com.marketplace.domain.classifiedad.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.cqrs.event.VersionedEvent;

import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonSerialize(as = ImmutableClassifiedApproved.class)
@JsonDeserialize(as = ImmutableClassifiedApproved.class)
public interface ClassifiedApproved extends VersionedEvent {

  UUID getId();

  UUID getUserId();

}
