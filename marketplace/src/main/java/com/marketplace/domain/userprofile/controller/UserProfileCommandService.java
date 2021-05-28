package com.marketplace.domain.userprofile.controller;

import com.marketplace.cqrs.command.CommandHandlerResult;
import com.marketplace.cqrs.command.ImmutableCommandHandlerResult;
import com.marketplace.cqrs.event.EventId;
import com.marketplace.cqrs.event.VersionedEvent;
import com.marketplace.cqrs.framework.AggregateRoot;
import com.marketplace.domain.AggregateStoreRepository;
import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.DisplayName;
import com.marketplace.domain.userprofile.UserProfile;
import com.marketplace.domain.userprofile.entity.UserProfileEntity;
import com.marketplace.domain.userprofile.repository.UserProfileQueryRepository;
import io.vavr.control.Try;
import java.util.Optional;
import java.util.UUID;
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
                updateUserFullName(command, userProfile);
                return doTryUpdates(userProfile);
            });
    }

    public CommandHandlerResult<UpdateUserProfileResult> handle(UpdateUserDisplayNameCommand command) {
        return aggregateStoreRepository
            .load(UserId.from(command.getUserId()))
            .map(it -> (UserProfile) it)
            .flatMap(userProfile -> updateUserDisplayName(command, userProfile))
            .orElse(ImmutableCommandHandlerResult.<UpdateUserProfileResult>builder()
                .result(ImmutableUpdateUserProfileResult.builder().id(command.getUserId()).build())
                .isSuccessful(false)
                .message("user with id " + command.getUserId().toString() + " was not found to be updated.")
                .build());
    }

    private Optional<ImmutableCommandHandlerResult<UpdateUserProfileResult>> updateUserFullName(
        UpdateUserFullNameCommand command, UserProfile userProfile) {
        userProfile.updateUserFullName(command.fullName());
        return doUserProfileUpdate(command.getUserId(), userProfile);
    }

    private Optional<ImmutableCommandHandlerResult<UpdateUserProfileResult>> updateUserDisplayName(
        UpdateUserDisplayNameCommand command, UserProfile userProfile) {
        var displayName = new DisplayName(command.getDisplayName());
        userProfile.updateDisplayName(displayName);
        return doUserProfileUpdate(command.getUserId(), userProfile);
    }

    private Try<UpdateUserProfileResult> doTryUpdates(UserProfile userProfile) {

        return Try.of(() -> aggregateStoreRepository.add(userProfile))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> ImmutableUpdateUserProfileResult.builder()
                .id(userProfile.getId().id())
                .build());
    }

    private Optional<ImmutableCommandHandlerResult<UpdateUserProfileResult>> doUserProfileUpdate(
        UUID userId, UserProfile userProfile) {
        return aggregateStoreRepository.add(userProfile)
            .map(user -> ImmutableCommandHandlerResult.<UpdateUserProfileResult>builder()
                .result(ImmutableUpdateUserProfileResult.builder().id(userId).build())
                .isSuccessful(true)
                .build())
            .or(() -> Optional.of(ImmutableCommandHandlerResult.<UpdateUserProfileResult>builder()
                .result(ImmutableUpdateUserProfileResult.builder().id(userId).build())
                .isSuccessful(false)
                .message("failed to update user, please try again")
                .build()));
    }


}
