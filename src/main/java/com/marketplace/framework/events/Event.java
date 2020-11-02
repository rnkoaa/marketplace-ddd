package com.marketplace.framework.events;

import java.util.UUID;

public sealed interface Event
        permits ClassifiedAdTextUpdated,
                ClassifiedAdCreated,
                ClassifiedAdTitleChanged,
                ClassifiedAdPriceUpdated,
                ClassifiedAdSentForReview {
    UUID getId();
}


