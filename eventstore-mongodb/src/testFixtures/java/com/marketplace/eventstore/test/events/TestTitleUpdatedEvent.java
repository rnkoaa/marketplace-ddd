package com.marketplace.eventstore.test.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableTestTitleUpdatedEvent.class)
@JsonDeserialize(as = ImmutableTestTitleUpdatedEvent.class)
public interface TestTitleUpdatedEvent extends VersionedEvent {

    String getTitle();
}
