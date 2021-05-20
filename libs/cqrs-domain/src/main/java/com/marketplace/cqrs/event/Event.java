package com.marketplace.cqrs.event;

import java.time.Instant;
import java.util.UUID;
import org.immutables.value.Value;

public interface Event {

    /**
     * @return time when event was created
     */

    @Value.Default
    default Instant getCreatedAt() {
        return Instant.now();
    }

    /**
     * @return the id of the current event
     */
    UUID getId();

    /**
     * @return the id of the aggregate from which this event was dispatched
     */
    UUID getAggregateId();

    @Value.Default
    default String getStreamId() {
        if(getAggregateName().contains(":")) {
            return getAggregateName();
        }
        return String.format("%s:%s", getAggregateName(), getAggregateId().toString());
    }

    /**
     * @return name of the class from which this aggregate was published from
     */
    @Value.Default
    default String getAggregateName() {
        return getClass().getSimpleName();
    }
}


