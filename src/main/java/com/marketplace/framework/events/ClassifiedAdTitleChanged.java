package com.marketplace.framework.events;

import java.util.UUID;

public final class ClassifiedAdTitleChanged implements Event {
    private final UUID id;
    private final String title;

    public ClassifiedAdTitleChanged(UUID id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
