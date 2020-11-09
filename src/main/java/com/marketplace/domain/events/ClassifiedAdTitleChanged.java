package com.marketplace.domain.events;

import com.marketplace.event.Event;
import lombok.Value;

import java.util.UUID;

@Value
public class ClassifiedAdTitleChanged implements Event {
    UUID id;
    String title;

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
