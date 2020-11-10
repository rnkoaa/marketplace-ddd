package com.marketplace.domain.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.marketplace.event.Event;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
@JsonDeserialize(builder = ClassifiedAdSentForReview.ClassifiedAdSentForReviewBuilder.class)
public class ClassifiedAdSentForReview implements Event {
    UUID id;

    public ClassifiedAdSentForReview(UUID id) {
        this.id = id;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class ClassifiedAdSentForReviewBuilder {

    }
}
