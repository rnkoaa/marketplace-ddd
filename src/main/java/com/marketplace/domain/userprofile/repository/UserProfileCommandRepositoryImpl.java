package com.marketplace.domain.userprofile.repository;

import com.marketplace.cqrs.event.VersionedEvent;
import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.UserProfile;
import com.marketplace.eventstore.framework.Result;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.eventstore.framework.event.EventStream;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class UserProfileCommandRepositoryImpl implements UserProfileCommandRepository {

    private final EventStore<VersionedEvent> eventEventStore;

    @Inject
    public UserProfileCommandRepositoryImpl(EventStore<VersionedEvent> eventEventStore) {
        this.eventEventStore = eventEventStore;
    }

    @Override
    public boolean exists(UserId id) {
        return load(id).isPresent();
    }

    @Override
    public Optional<UserProfile> load(UserId id) {
        String streamId = getStreamIdByClass(UserProfile.class, id.toString());
        EventStream<VersionedEvent> eventStream = eventEventStore.load(streamId);
        if (eventStream.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(new UserProfile(eventStream.getEvents()));
    }

    @Override
    public Optional<UserProfile> add(UserProfile userProfile) {
        var streamId = getStreamId(userProfile, userProfile.getId().toString());
        List<VersionedEvent> changes = userProfile.getChanges();
        Result<Boolean> append = eventEventStore.append(streamId, userProfile.getVersion(), changes);
        return append.toOptional()
            .filter(it -> it)
            .map(res -> {
                userProfile.clearChanges();
                return userProfile;
            });
    }

    private String getStreamIdByClass(Class<?> clzz, String aggregateId) {
        return String.format("%s:%s", clzz.getSimpleName(), aggregateId);
    }
    private String getStreamId(Object object, String aggregateId) {
        return String.format("%s:%s", object.getClass().getSimpleName(), aggregateId);
    }

    @Override
    public void deleteAll() {

    }
}
