package com.marketplace.domain;

import com.marketplace.event.Event;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ClassifiedApproved implements Event {
    UUID id;
    UUID userId;

    @Override
    public UUID getId() {
        return null;
    }
}
/*
public record ClassifiedApproved(UUID id, UserId value) {
    public ClassifiedApproved {
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }
    }

    public static ClassifiedApproved fromString(ClassifiedAdId id, String value) {
        return new ClassifiedApproved(id.id(), UserId.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
*/
