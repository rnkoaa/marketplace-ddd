package com.marketplace.domain.userprofile.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.marketplace.cqrs.event.VersionedEvent;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableDeleteAllUsersEvent.class)
public interface DeleteAllUsersEvent extends VersionedEvent {

}
