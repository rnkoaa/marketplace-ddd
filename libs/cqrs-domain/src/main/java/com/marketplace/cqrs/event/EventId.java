package com.marketplace.cqrs.event;

import java.util.Objects;
import java.util.UUID;

public abstract class EventId {

    private final UUID id;

    public EventId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        this.id = id;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    public UUID id() {
        return id;
    }

    public abstract String getStreamId();

    public abstract String getAggregateName();

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (EventId) obj;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
