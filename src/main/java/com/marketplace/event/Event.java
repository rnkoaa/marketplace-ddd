package com.marketplace.event;

import java.util.UUID;

public interface Event {
    UUID getId();

    default String name() {
        return getClass().getSimpleName();
    }
}


