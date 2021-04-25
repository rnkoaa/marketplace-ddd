package com.marketplace.cqrs.event;

import org.immutables.value.Value;

@Value.Immutable
public interface TypedEvent {

  String getType();

  int getSequenceId();

  String getEventBody();

}
