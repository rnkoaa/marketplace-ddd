package com.marketplace.domain.shared;

import com.marketplace.cqrs.event.EventId;
import com.marketplace.domain.userprofile.UserProfile;
import java.util.UUID;

public final class UserId extends EventId {

    public UserId(UUID id) {
        super(id);
    }

    public static UserId newId() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId from(UUID id) {
        return new UserId(id);
    }

    public static UserId from(String uuid) {
        return new UserId(UUID.fromString(uuid));
    }

    @Override
    public String getStreamId() {
        return String.format("%s:%s", UserProfile.class.getSimpleName(), super.id());
    }

    @Override
    public String getAggregateName() {
        return UserProfile.class.getSimpleName();
    }
}
