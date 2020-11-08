package com.marketplace.framework;

public interface InternalEventHandler<T> {
    void handle(T event);
}
