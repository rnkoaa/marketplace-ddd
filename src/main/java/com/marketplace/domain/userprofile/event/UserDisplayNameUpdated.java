package com.marketplace.domain.userprofile.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.event.VersionedEvent;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonSerialize(as = ImmutableUserDisplayNameUpdated.class)
@JsonDeserialize(as = ImmutableUserDisplayNameUpdated.class)
public interface UserDisplayNameUpdated extends VersionedEvent {

  UUID getUserId();

  String getDisplayName();
}
