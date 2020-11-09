package com.marketplace.framework;

import com.marketplace.event.Event;

public abstract class Entity<T, U extends Event> implements InternalEventHandler<U> {
    private final EventApplier applier;

    public Entity(EventApplier eventApplier) {
        this.applier = eventApplier;
    }

    public void apply(U event) {
        when(event);
        this.applier.apply(event);
    }

    public abstract void when(U event);

    @Override
    public void handle(U event) {
        when(event);
    }
}
