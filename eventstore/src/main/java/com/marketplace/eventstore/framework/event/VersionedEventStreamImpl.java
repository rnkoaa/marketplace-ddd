package com.marketplace.eventstore.framework.event;

import com.marketplace.cqrs.event.VersionedEvent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class VersionedEventStreamImpl implements EventStream<VersionedEvent> {

    private List<VersionedEvent> events;
    private final String id;
    private final String name;
    private int version;
    private final Instant createdAt;
    private Instant updatedAt;

    public VersionedEventStreamImpl(String id, String name, int version, List<VersionedEvent> events) {
        this(id, name, version, Instant.now(), Instant.now());
        if (this.events != null) {
            this.events = events;
        }
    }

    public VersionedEventStreamImpl(
        String id, String name, int version, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.events = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public List<VersionedEvent> getEvents() {
        return events;
    }

    @Override
    public Instant createdAt() {
        return createdAt;
    }

    @Override
    public Instant updatedAt() {
        return updatedAt;
    }

    @Override
    public boolean isEmpty() {
        return this.events.isEmpty();
    }

    @Override
    public void append(VersionedEvent event, int expectedVersion) {
        this.version = expectedVersion;
        this.updatedAt = Instant.now();
        this.events.add(event);
    }

    @Override
    public int size() {
        return events.size();
    }
}
