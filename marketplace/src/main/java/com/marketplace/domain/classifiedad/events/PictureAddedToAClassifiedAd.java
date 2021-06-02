package com.marketplace.domain.classifiedad.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.cqrs.event.VersionedEvent;
import com.marketplace.domain.classifiedad.ClassifiedAd;
import java.util.UUID;
import org.immutables.value.Value;
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

    @Value.Default
    default int getOrder() {
        return 0;
    }

    @Value.Default
    @Override
    default String getAggregateName() {
        return ClassifiedAd.class.getSimpleName();
    }


}
