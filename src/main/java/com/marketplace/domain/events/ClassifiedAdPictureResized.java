package com.marketplace.domain.events;

import com.marketplace.event.Event;
import lombok.Value;

import java.util.UUID;

@Value
public class ClassifiedAdPictureResized implements Event {
    UUID ClassifiedAdId;
    UUID PictureId;
    int Height;
    int Width;

    @Override
    public UUID getId() {
        return getClassifiedAdId();
    }
}
