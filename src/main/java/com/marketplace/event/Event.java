package com.marketplace.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.marketplace.domain.classifiedad.events.*;

import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClassifiedAdCreated.class),
        @JsonSubTypes.Type(value = ClassifiedAdTextUpdated.class),
        @JsonSubTypes.Type(value = ClassifiedAdTitleChanged.class),
        @JsonSubTypes.Type(value = ClassifiedAdPriceUpdated.class),
        @JsonSubTypes.Type(value = ClassifiedAdSentForReview.class),
        @JsonSubTypes.Type(value = ClassifiedAdPictureResized.class),
        @JsonSubTypes.Type(value = ClassifiedApproved.class),
        @JsonSubTypes.Type(value = PictureAddedToAClassifiedAd.class)
})
public interface Event {
    UUID getId();

    default String name() {
        return getClass().getSimpleName();
    }
}


