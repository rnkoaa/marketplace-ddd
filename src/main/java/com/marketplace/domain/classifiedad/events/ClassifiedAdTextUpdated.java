package com.marketplace.domain.classifiedad.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.marketplace.event.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
@JsonDeserialize(builder = ClassifiedAdTextUpdated.ClassifiedAdTextUpdatedBuilder.class)
public class ClassifiedAdTextUpdated implements Event {
    UUID id;
    String text;

    @Override
    public UUID getId() {
        return id;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class ClassifiedAdTextUpdatedBuilder {

    }
}
