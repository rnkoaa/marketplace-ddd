package com.marketplace.eventstore.impl.fixtures.classifiedad;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.cqrs.event.VersionedEvent;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableClassifiedAdTextUpdated.class)
@JsonDeserialize(as = ImmutableClassifiedAdTextUpdated.class)
public interface ClassifiedAdTextUpdated extends VersionedEvent {

    String getText();

}
