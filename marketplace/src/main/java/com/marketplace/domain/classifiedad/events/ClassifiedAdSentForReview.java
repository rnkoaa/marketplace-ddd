package com.marketplace.domain.classifiedad.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.cqrs.event.VersionedEvent;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableClassifiedAdSentForReview.class)
@JsonSerialize(as = ImmutableClassifiedAdSentForReview.class)
public interface ClassifiedAdSentForReview extends VersionedEvent {

  UUID getId();

}
