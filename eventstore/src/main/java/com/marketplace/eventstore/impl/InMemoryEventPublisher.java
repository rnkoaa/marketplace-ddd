package com.marketplace.eventstore.impl;

import com.google.common.eventbus.EventBus;
import com.marketplace.cqrs.event.Event;
import com.marketplace.eventstore.framework.event.EventListener;
import com.marketplace.eventstore.framework.event.EventPublisher;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class InMemoryEventPublisher implements EventPublisher<Event>, AutoCloseable {

    private final EventBus eventBus;
    private final List<EventListener> listeners;

    public InMemoryEventPublisher(EventBus eventBus) {
        this.eventBus = eventBus;
        this.listeners = new ArrayList<>();
    }

    public void registerListener(EventListener eventListener) {
        this.eventBus.register(eventListener);
        this.listeners.add(eventListener);
    }

    @Override
    public void publish(String streamId, Event event) {
        eventBus.post(event);
    }

    @Override
    public void publish(String streamId, List<Event> event) {
        event.forEach(eventBus::post);
    }

    @Override
    public void close() {
        this.listeners.forEach(eventBus::unregister);
    }
}