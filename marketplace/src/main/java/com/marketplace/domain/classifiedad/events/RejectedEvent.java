package com.marketplace.domain.classifiedad.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.marketplace.cqrs.event.VersionedEvent;
import com.marketplace.domain.shared.UserId;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableRejectedEvent.class)
public interface RejectedEvent extends VersionedEvent {

    UserId getApprover();

    String rejectedMessage();
}
