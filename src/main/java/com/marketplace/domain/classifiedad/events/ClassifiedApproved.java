package com.marketplace.domain.classifiedad.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.marketplace.event.Event;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
@JsonDeserialize(builder = ClassifiedApproved.ClassifiedApprovedBuilder.class)
public class ClassifiedApproved implements Event {
    UUID id;
    UUID userId;

    @Override
    public UUID getId() {
        return null;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class ClassifiedApprovedBuilder {

    }
}
