package com.marketplace.domain.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.marketplace.event.Event;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
@JsonDeserialize(builder = ClassifiedAdTitleChanged.ClassifiedAdTitleChangedBuilder.class)
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

    @JsonPOJOBuilder(withPrefix = "")
    public static class ClassifiedAdTitleChangedBuilder {

    }
}
