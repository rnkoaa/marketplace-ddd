package com.marketplace.domain.classifiedad.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.cqrs.event.VersionedEvent;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableClassifiedAdTitleChanged.class)
@JsonSerialize(as = ImmutableClassifiedAdTitleChanged.class)
public interface ClassifiedAdTitleChanged extends VersionedEvent {

  UUID getId();

  String getTitle();

}
