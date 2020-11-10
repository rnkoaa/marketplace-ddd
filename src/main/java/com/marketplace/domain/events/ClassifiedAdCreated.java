package com.marketplace.domain.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.marketplace.domain.ClassifiedAd;
import com.marketplace.event.Event;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = ClassifiedAdCreated.ClassifiedAdCreatedBuilder.class)
public class ClassifiedAdCreated implements Event {
    UUID id;
    UUID userId;


    public ClassifiedAdCreated(UUID id, UUID userId) {
        this.id = id;
        this.userId = userId;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    @JsonPOJOBuilder(withPrefix = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ClassifiedAdCreatedBuilder {

    }
}
