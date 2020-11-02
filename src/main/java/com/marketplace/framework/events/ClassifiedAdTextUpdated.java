package com.marketplace.framework.events;

import java.util.UUID;

public final class ClassifiedAdTextUpdated implements Event {
    private final UUID id;
    private final String AdText;

    public ClassifiedAdTextUpdated(UUID id, String adText) {
        this.id = id;
        AdText = adText;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public String getAdText() {
        return AdText;
    }
}
