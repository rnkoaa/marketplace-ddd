package com.marketplace.cqrs.framework;

import com.marketplace.cqrs.event.VersionedEvent;

@FunctionalInterface
public interface EventApplier {

    void apply(VersionedEvent event);
}
