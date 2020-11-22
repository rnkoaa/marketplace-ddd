package com.marketplace.domain.userprofile.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.marketplace.domain.classifiedad.events.ClassifiedAdCreated;
import com.marketplace.event.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = ClassifiedAdCreated.ClassifiedAdCreatedBuilder.class)
@AllArgsConstructor
public class ProfilePhotoUploaded implements Event {
    UUID userId;
    String photoUrl;

    @Override
    public UUID getId() {
        return userId;
    }

}
