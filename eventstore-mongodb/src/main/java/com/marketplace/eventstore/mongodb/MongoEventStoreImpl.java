package com.marketplace.eventstore.mongodb;

import com.marketplace.eventstore.framework.OperationResult;
import com.marketplace.eventstore.framework.event.Event;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.eventstore.framework.event.EventStream;

import java.util.List;

public class MongoEventStoreImpl implements EventStore<Event> {

    @Override
    public EventStream<Event> load(String streamId) {
        return null;
    }

    @Override
    public EventStream<Event> load(String streamId, int fromVersion) {
        return null;
    }

    @Override
    public OperationResult append(String streamId, int expectedVersion, List<Event> events) {
        return null;
    }

    @Override
    public OperationResult append(String streamId, int expectedVersion, Event event) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public int streamSize(String streamId) {
        return 0;
    }

    @Override
    public int getVersion(String streamId) {
        return 0;
    }

    @Override
    public int nextVersion(String streamId) {
        return 0;
    }

    @Override
    public OperationResult publish(String streamId, Event event) {
        return null;
    }

    @Override
    public OperationResult publish(String streamId, int expectedVersion, List<Event> events) {
        return null;
    }

    @Override
    public OperationResult publish(String streamId, int expectedVersion, Event event) {
        return null;
    }
}
