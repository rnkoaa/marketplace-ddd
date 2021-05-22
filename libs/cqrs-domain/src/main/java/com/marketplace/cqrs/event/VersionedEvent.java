package com.marketplace.cqrs.event;

import java.time.Instant;
import org.immutables.value.Value;

public interface VersionedEvent extends Event {

    @Value.Default
    default long getVersion() {
        return 0;
    }

    @Value.Default
    default boolean isNew() {
        return false;
    }

    @Value.Default
    @Override
    default String getAggregateName() {
        return "ClassifiedAd";
    }

    @Value.Default
    @Override
    default Instant getCreatedAt() {
        return Instant.now();
    }

}
