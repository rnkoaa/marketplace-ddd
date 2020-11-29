package com.marketplace.eventstore.framework.event;

import java.time.Instant;
import java.util.UUID;

public interface Event {
  /** @return time when event was created */
  default Instant createdAt() {
    return Instant.now();
  }

  /** @return the id of the current event */
  UUID getId();

  /** @return the id of the aggregate from which this event was dispatched */
  UUID getAggregateId();

  /** @return name of the class from which this aggregate was published from */
  default String aggregateName() {
    return getClass().getSimpleName();
  }
}
