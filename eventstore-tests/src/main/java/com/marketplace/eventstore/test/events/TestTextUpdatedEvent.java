package com.marketplace.eventstore.test.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.cqrs.event.VersionedEvent;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableTestTextUpdatedEvent.class)
@JsonDeserialize(as = ImmutableTestTextUpdatedEvent.class)
public interface TestTextUpdatedEvent extends VersionedEvent {

    String getText();

}
