package com.marketplace.cqrs.event;

import java.time.Instant;
import java.util.UUID;


public interface Event {

    /**
     * @return time when event was created
     */
    default Instant getCreatedAt() {
        return Instant.now();
    }

    default long getVersion() {
        return 0;
    }



    /**
     * @return the id of the current event
     */
    UUID getId();

    /**
     * @return the id of the aggregate from which this event was dispatched
     */
    UUID getAggregateId();

    default String getStreamId(){
        return String.format("%s:%s", getAggregateName(), getAggregateId().toString());
    }

    /**
     * @return name of the class from which this aggregate was published from
     */
    default String getAggregateName() {
        return getClass().getSimpleName();
    }
}


