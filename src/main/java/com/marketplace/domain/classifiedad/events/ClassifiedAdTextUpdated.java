package com.marketplace.domain.classifiedad.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.event.VersionedEvent;
import java.util.UUID;
import org.immutables.value.Value.Immutable;


@Immutable
@JsonDeserialize(as = ImmutableClassifiedAdTextUpdated.class)
@JsonSerialize(as = ImmutableClassifiedAdTextUpdated.class)
public interface ClassifiedAdTextUpdated extends VersionedEvent {

  UUID getId();

  String getText();

}
