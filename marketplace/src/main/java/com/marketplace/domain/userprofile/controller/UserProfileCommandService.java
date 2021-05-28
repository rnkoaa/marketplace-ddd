package com.marketplace.domain.userprofile.controller;

import com.marketplace.domain.AggregateStoreRepository;
import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.DisplayName;
import com.marketplace.domain.userprofile.FullName;
import com.marketplace.domain.userprofile.UserProfile;
import com.marketplace.domain.userprofile.entity.UserProfileEntity;
import com.marketplace.domain.userprofile.repository.UserProfileQueryRepository;
import io.vavr.control.Try;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class UserProfileCommandService {

    private final AggregateStoreRepository aggregateStoreRepository;
    private final UserProfileQueryRepository userProfileQueryRepository;

    @Inject
    public UserProfileCommandService(UserProfileQueryRepository userProfileQueryRepository,
        AggregateStoreRepository aggregateStoreRepository
    ) {
        this.userProfileQueryRepository = userProfileQueryRepository;
        this.aggregateStoreRepository = aggregateStoreRepository;
    }

    public Try<CreateUserProfileResult> handle(CreateUserProfileCommand command) {
        var userId = UserId.newId();

        Optional<UserProfileEntity> existingUserProfile = userProfileQueryRepository
            .findByDisplayName(command.getDisplayName());

        if (existingUserProfile.isPresent()) {
            return Try.failure(new DuplicateDisplayNameException(command.getDisplayName() + " already exists"));
        }

        var displayName = new DisplayName(command.getDisplayName());
        UserProfile userProfile = new UserProfile(userId, command.fullName(), displayName);
        return Try.of(() -> aggregateStoreRepository.add(userProfile))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> ImmutableCreateUserProfileResult.builder()
                .id(user.getId().id())
                .build());
    }

    public Try<UpdateUserProfileResult> handle(UpdateUserProfileCommand command) {
        Optional<UserProfile> load = aggregateStoreRepository
            .load(UserId.from(command.getUserId()))
            .map(it -> (UserProfile) it);
        return Try.ofSupplier(load::get)
            .flatMap(userProfile -> {
                userProfile.updatePhoto(command.getPhotoUrl());
                return doTryUpdates(userProfile);
            });
    }

    public Try<UpdateUserProfileResult> handle(UpdateUserFullNameCommand command) {
        Optional<UserProfile> load = aggregateStoreRepository
            .load(UserId.from(command.getUserId()))
            .map(it -> (UserProfile) it);
        return Try.ofSupplier(load::get)
            .flatMap(userProfile -> {
                userProfile.updateUserFullName(
                    new FullName(command.getFirstName(),
                        command.getMiddleName().orElse(""),
                        command.getLastName())
                );
                return doTryUpdates(userProfile);
            });
    }

    public Try<UpdateUserProfileResult> handle(UpdateUserDisplayNameCommand command) {
        Optional<UserProfile> loadedUserProfile = aggregateStoreRepository
            .load(UserId.from(command.getUserId()))
            .map(it -> (UserProfile) it);

        return Try.ofSupplier(loadedUserProfile::get)
            .flatMap(userProfile -> {
                userProfile.updateDisplayName(new DisplayName(command.getDisplayName()));
                return doTryUpdates(userProfile);
            })
            ;
    }

    private Try<UpdateUserProfileResult> doTryUpdates(UserProfile userProfile) {
        return Try.of(() -> aggregateStoreRepository.add(userProfile))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> ImmutableUpdateUserProfileResult.builder()
                .id(userProfile.getId().id())
                .build());
    }
}
