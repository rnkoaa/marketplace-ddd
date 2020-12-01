package com.marketplace.eventstore.framework.event;

import org.immutables.value.Value;

@Value.Immutable
public interface TypedEvent {

  String getType();

  default int getSequenceId() {
    return 0;
  }

  String getEventBody();

}
