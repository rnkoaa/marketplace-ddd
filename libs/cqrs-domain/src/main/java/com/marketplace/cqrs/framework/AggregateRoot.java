package com.marketplace.cqrs.framework;

import com.marketplace.cqrs.event.VersionedEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot<T, U extends VersionedEvent> implements InternalEventHandler<U>, EventApplier {

    private final List<VersionedEvent> changes;
    private int version;

    public int getVersion() {
        return version;
    }

    protected AggregateRoot() {
        version = 0;
        this.changes = new ArrayList<>();
    }

    public void apply(VersionedEvent event) {
        when(event);
        ensureValidState(event);
        changes.add(event);
        version++;
    }

    public abstract void ensureValidState(VersionedEvent event);

    public void clearChanges() {
        changes.clear();
    }

    public List<VersionedEvent> getChanges() {
        return changes;
    }

    public abstract void when(VersionedEvent event);

    protected void applyToEntity(InternalEventHandler<VersionedEvent> entity, VersionedEvent event) {
        entity.handle(event);
    }

    @Override
    public void handle(VersionedEvent event) {
        when(event);
    }
}
