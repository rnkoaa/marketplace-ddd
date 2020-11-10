package com.marketplace.domain.classifiedad.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.marketplace.event.Event;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
@JsonDeserialize(builder = ClassifiedAdPictureResized.ClassifiedAdPictureResizedBuilder.class)
public class ClassifiedAdPictureResized implements Event {
    UUID classifiedAdId;
    UUID pictureId;
    int height;
    int width;

    @Override
    public UUID getId() {
        return getClassifiedAdId();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class ClassifiedAdPictureResizedBuilder {

    }
}
