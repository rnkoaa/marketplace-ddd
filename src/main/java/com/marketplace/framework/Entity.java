package com.marketplace.framework;

import com.marketplace.framework.events.Event;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity<T> {
    private final List<Object> events;

    public Entity() {
        events = new ArrayList<>();
    }

    public void apply(Object event) {
        ensureValidState();
        events.add(event);
    }

    public abstract void ensureValidState();

    public void clearChanges() {
        events.clear();
    }

    public List<Object> getChanges() {
        return events;
    }

    public abstract void when(Event event);
}
