package com.marketplace.eventstore.impl.fixtures.classifiedad;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.cqrs.event.VersionedEvent;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableClassifiedAdTitleUpdated.class)
@JsonDeserialize(as = ImmutableClassifiedAdTitleUpdated.class)
public interface ClassifiedAdTitleUpdated extends VersionedEvent {
  UUID getId();
  String getTitle();

}
