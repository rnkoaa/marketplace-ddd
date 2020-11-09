package com.marketplace.domain.events;

import com.marketplace.event.Event;
import lombok.Value;

import java.util.UUID;

@Value
public class PictureAddedToAClassifiedAd implements Event {
    UUID classifiedAdId;
    UUID pictureId;
    String url;
    int height;
    int width;
    int order;

    @Override
    public UUID getId() {
        return classifiedAdId;
    }
}
