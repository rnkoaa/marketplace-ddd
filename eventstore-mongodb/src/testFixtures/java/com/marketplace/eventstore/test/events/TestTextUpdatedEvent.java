package com.marketplace.eventstore.test.events;

import com.marketplace.eventstore.framework.event.Event;
import org.immutables.value.Value;

@Value.Immutable
public interface TestTextUpdatedEvent extends Event {

    String getText();
}
