package com.marketplace.domain.classifiedad.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.cqrs.event.VersionedEvent;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutablePictureAddedToAClassifiedAd.class)
@JsonSerialize(as = ImmutablePictureAddedToAClassifiedAd.class)
public interface PictureAddedToAClassifiedAd extends VersionedEvent {
    UUID getClassifiedAdId();
    UUID getPictureId();
    String getUrl();
    int getHeight();
    int getWidth();
    int getOrder();
}