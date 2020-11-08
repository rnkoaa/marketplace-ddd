package com.marketplace.framework;

import com.marketplace.event.Event;

import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot<T, U extends Event> implements InternalEventHandler<U> {
    private final List<Event> changes;

    protected AggregateRoot() {
        this.changes = new ArrayList<>();
    }

    public void apply(Event event) {
        when(event);
        ensureValidState();
        changes.add(event);
    }

    public abstract void ensureValidState();

    public void clearChanges() {
        changes.clear();
    }

    public List<Event> getChanges() {
        return changes;
    }

    public abstract void when(Event event);

    protected void applyToEntity(InternalEventHandler<Event> entity, Event event) {
        entity.handle(event);
    }

    @Override
    public void handle(Event event) {
        when(event);
    }
}
