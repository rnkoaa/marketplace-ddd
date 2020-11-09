package com.marketplace.domain.events;

import com.marketplace.event.Event;
import lombok.Value;

import java.util.UUID;

@Value
public class ClassifiedAdTextUpdated implements Event {
    UUID id;
    String AdText;

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
