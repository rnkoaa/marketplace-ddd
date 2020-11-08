package com.marketplace.framework;

import java.util.function.Consumer;

public abstract class EventApplier<Object> implements Consumer<Object> {

    public abstract void apply(Object event);

    @Override
    public void accept(Object object) {
        apply(object);
    }
}
