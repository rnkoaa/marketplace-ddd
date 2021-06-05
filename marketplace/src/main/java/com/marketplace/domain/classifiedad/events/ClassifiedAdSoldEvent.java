package com.marketplace.domain.classifiedad.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.cqrs.event.VersionedEvent;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableClassifiedAdSoldEvent.class)
@JsonSerialize(as = ImmutableClassifiedAdSoldEvent.class)
public interface ClassifiedAdSoldEvent extends VersionedEvent {
    UUID getId();

}
