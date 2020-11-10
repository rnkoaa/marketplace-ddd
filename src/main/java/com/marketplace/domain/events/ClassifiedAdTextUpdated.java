package com.marketplace.domain.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.marketplace.event.Event;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
@JsonDeserialize(builder = ClassifiedAdTextUpdated.ClassifiedAdTextUpdatedBuilder.class)
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

    @JsonPOJOBuilder(withPrefix = "")
    public static class ClassifiedAdTextUpdatedBuilder {

    }
}
