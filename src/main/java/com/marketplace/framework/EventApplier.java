package com.marketplace.framework;

import com.marketplace.event.VersionedEvent;

@FunctionalInterface
public interface EventApplier {

    void apply(VersionedEvent event);
}
