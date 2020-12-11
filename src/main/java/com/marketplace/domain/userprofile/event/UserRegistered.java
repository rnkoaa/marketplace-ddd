package com.marketplace.domain.userprofile.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.event.Event;

import com.marketplace.event.VersionedEvent;
import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonSerialize(as = ImmutableUserRegistered.class)
@JsonDeserialize(as = ImmutableUserRegistered.class)
public interface UserRegistered extends VersionedEvent {

  UUID getUserId();

  String getFirstName();

  Optional<String> getMiddleName();

  String getLastName();

  String getDisplayName();

}
