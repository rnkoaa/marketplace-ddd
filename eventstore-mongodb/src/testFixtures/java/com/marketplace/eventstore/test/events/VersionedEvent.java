package com.marketplace.eventstore.test.events;

import java.time.Instant;
import org.immutables.value.Value;

public interface VersionedEvent extends Event {

  @Value.Default
  @Override
  default long getVersion() {
    return 0;
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
