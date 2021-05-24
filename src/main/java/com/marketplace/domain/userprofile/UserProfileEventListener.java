package com.marketplace.domain.userprofile;

import com.google.common.eventbus.Subscribe;
import com.marketplace.domain.userprofile.entity.ImmutableUserProfileEntity;
import com.marketplace.domain.userprofile.entity.ImmutableUserProfileEntity.Builder;
import com.marketplace.domain.userprofile.entity.UserProfileEntity;
import com.marketplace.domain.userprofile.event.ProfilePhotoUploaded;
import com.marketplace.domain.userprofile.event.UserDisplayNameUpdated;
import com.marketplace.domain.userprofile.event.UserFullNameUpdated;
import com.marketplace.domain.userprofile.event.UserRegistered;
import com.marketplace.domain.userprofile.repository.UserProfileQueryRepository;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@Singleton
@SuppressWarnings("UnstableApiUsage")
public class UserProfileEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileEventListener.class);
    private final UserProfileQueryRepository userProfileQueryRepository;

    @Inject
    public UserProfileEventListener(UserProfileQueryRepository userProfileQueryRepository) {
        this.userProfileQueryRepository = userProfileQueryRepository;
    }

    @Subscribe
    public void onUserCreated(UserRegistered event) {
        UserProfileEntity entity = createFromEvent(event);
        Optional<UserProfileEntity> maybeSaved = userProfileQueryRepository.save(entity);
        if (maybeSaved.isEmpty()) {
            LOGGER.info("error saving userprofile {}", event);
        }
    }

    @Subscribe
    public void onUserFullNameUpdated(UserFullNameUpdated event) {
        Optional<UserProfileEntity> maybeUpdateUserProfile = userProfileQueryRepository.findById(event.getUserId())
            .flatMap(userProfileEntity -> {
                String middleName = "";
                if (event.getMiddleName().isPresent()) {
                    middleName = event.getMiddleName().get();
                }
                UserProfileEntity modifiedUserProfileEntity = ImmutableUserProfileEntity.copyOf(userProfileEntity)
                    .withFirstName(event.getFirstName())
                    .withLastName(event.getLastName())
                    .withMiddleName(middleName);

                return userProfileQueryRepository.save(modifiedUserProfileEntity);
            });
        if (maybeUpdateUserProfile.isEmpty()) {
            LOGGER.info("error updating userprofile {}", event);
        }
    }

    @Subscribe
    public void onDisplayNameUpdated(UserDisplayNameUpdated event) {
        Optional<UserProfileEntity> maybeUpdateUserProfile = userProfileQueryRepository.findById(event.getUserId())
            .flatMap(userProfileEntity -> {
                UserProfileEntity modifiedUserProfileEntity = ImmutableUserProfileEntity.copyOf(userProfileEntity)
                    .withDisplayName(event.getDisplayName());
                return userProfileQueryRepository.save(modifiedUserProfileEntity);
            });
        if (maybeUpdateUserProfile.isEmpty()) {
            LOGGER.info("error updating display name {}", event);
        }
    }

    @Subscribe
    public void onPhotoAdded(ProfilePhotoUploaded event) {
        Optional<UserProfileEntity> maybeUpdateUserProfile = userProfileQueryRepository.findById(event.getUserId())
            .flatMap(userProfileEntity -> {
                UserProfileEntity modifiedUserProfileEntity = ImmutableUserProfileEntity.copyOf(userProfileEntity)
                    .withPhotoUrl(event.getPhotoUrl());
                return userProfileQueryRepository.save(modifiedUserProfileEntity);
            });
        if (maybeUpdateUserProfile.isEmpty()) {
            LOGGER.info("error updating display name {}", event);
        }
    }

    @Subscribe
    public static UserProfileEntity createFromEvent(UserRegistered event) {
        Builder builder = ImmutableUserProfileEntity.builder()
            .id(event.getUserId())
            .displayName(event.getDisplayName())
            .firstName(event.getFirstName())
            .lastName(event.getLastName());

        event.getMiddleName().ifPresent(builder::middleName);
        return builder.build();
    }

}
