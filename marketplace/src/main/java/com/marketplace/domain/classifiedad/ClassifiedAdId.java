package com.marketplace.domain.classifiedad;

import com.marketplace.cqrs.event.EventId;
import java.util.UUID;

public final class ClassifiedAdId extends EventId {

    public ClassifiedAdId(UUID id) {
        super(id);
    }

    public ClassifiedAdId() {
        this(UUID.randomUUID());
    }

    public static ClassifiedAdId newClassifiedAdId() {
        return new ClassifiedAdId(UUID.randomUUID());
    }

    public static ClassifiedAdId from(UUID value) {
        return new ClassifiedAdId(value);
    }

    public static ClassifiedAdId fromString(String uuid) {
        var id = UUID.fromString(uuid);
        return new ClassifiedAdId(id);
    }

    @Override
    public String getStreamId() {
        return String.format("%s:%s", ClassifiedAd.class.getSimpleName(), super.id());
    }

    @Override
    public String getAggregateName() {
        return ClassifiedAd.class.getSimpleName();
    }

}
