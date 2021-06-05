package com.marketplace.domain.shared;

import com.marketplace.cqrs.event.EventId;
import com.marketplace.domain.userprofile.UserProfile;
import java.util.UUID;
import org.jooq.User;

public final class UserId extends EventId {

    public static UserId EMPTY_VALUE = new UserId(UUID.fromString(IdGenerator.EMPTY));

    public UserId(UUID id) {
        super(id);
    }

    public static UserId from(UUID id) {
        return new UserId(id);
    }

    public static UserId from(String uuid) {
        return new UserId(UUID.fromString(uuid));
    }

    public static UserId newUserId() {
        return new UserId(UUID.randomUUID());
    }

    public boolean isValid() {
        return id() != null && !id().toString().equals(IdGenerator.EMPTY);
    }

    @Override
    public String toString() {
        return id() != null ? id().toString() : "";
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
