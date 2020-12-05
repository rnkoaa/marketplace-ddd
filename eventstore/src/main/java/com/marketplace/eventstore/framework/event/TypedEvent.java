package com.marketplace.eventstore.framework.event;

import org.immutables.value.Value;

@Value.Immutable
public interface TypedEvent {

  String getType();

  int getSequenceId();

  String getEventBody();

}
