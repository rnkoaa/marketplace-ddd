package com.marketplace.cqrs.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.Instant;
import java.util.UUID;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ImplementationVisibility;

@JsonSerialize // Jackson automatic integration, why not?
@Value.Style(
    typeAbstract = "Abstract*",
    typeImmutable = "*",
    visibility = ImplementationVisibility.PUBLIC)
public interface Event {

    /**
     * @return time when event was created
     */

    @Value.Default
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


