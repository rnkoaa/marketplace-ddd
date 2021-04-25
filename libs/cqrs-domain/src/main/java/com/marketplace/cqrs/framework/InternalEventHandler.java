package com.marketplace.cqrs.framework;

public interface InternalEventHandler<T> {
    void handle(T event);
}
