package com.marketplace.framework;

import com.marketplace.event.Event;

@FunctionalInterface
public interface EventApplier {

    void apply(Event event);
}
