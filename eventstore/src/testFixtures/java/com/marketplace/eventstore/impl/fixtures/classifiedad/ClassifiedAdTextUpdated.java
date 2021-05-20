package com.marketplace.eventstore.impl.fixtures.classifiedad;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.cqrs.event.Event;

import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableClassifiedAdTextUpdated.class)
@JsonDeserialize(as = ImmutableClassifiedAdTextUpdated.class)
public interface ClassifiedAdTextUpdated extends Event {

    String getText();

}
