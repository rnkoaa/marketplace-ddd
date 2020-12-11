package com.marketplace.eventstore.test.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableTestCreatedEvent.class)
@JsonDeserialize(as = ImmutableTestCreatedEvent.class)
public interface TestCreatedEvent extends VersionedEvent {


}
