package com.marketplace.domain.userprofile.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.event.Event;

import com.marketplace.event.VersionedEvent;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableProfilePhotoUploaded.class)
@JsonSerialize(as = ImmutableProfilePhotoUploaded.class)
public interface ProfilePhotoUploaded extends VersionedEvent {

  UUID getUserId();

  String getPhotoUrl();

}
