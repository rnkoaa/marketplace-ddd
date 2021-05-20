package com.marketplace.eventstore.impl.fixtures.classifiedad;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.cqrs.event.VersionedEvent;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableClassifiedAdCreated.class)
@JsonDeserialize(as = ImmutableClassifiedAdCreated.class)
//@Value.Style(builder = "new") // builder has to have constructor
//@JsonDeserialize(builder = ImmutableClassifiedAdCreated.Builder.class)
public interface ClassifiedAdCreated extends VersionedEvent {

    UUID getOwner();

}
