package com.marketplace.domain.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.marketplace.event.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
