package com.marketplace.domain.userprofile.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.event.Event;

import com.marketplace.event.VersionedEvent;
import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableUserFullNameUpdated.class)
@JsonSerialize(as = ImmutableUserFullNameUpdated.class)
public interface UserFullNameUpdated extends VersionedEvent {

  UUID getUserId();

  String getFirstName();

  String getLastName();

  Optional<String> getMiddleName();
}
