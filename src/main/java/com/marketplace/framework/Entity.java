package com.marketplace.framework;

public abstract class Entity<T, U> implements InternalEventHandler<U> {
    private final EventApplier<U> applier;

    public Entity(EventApplier<U> applier) {
        this.applier = applier;
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
