package com.marketplace.domain.userprofile.controller;

import com.marketplace.cqrs.command.CommandHandlerResult;
import com.marketplace.cqrs.command.ImmutableCommandHandlerResult;
import com.marketplace.domain.AggregateStoreRepository;
import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.DisplayName;
import com.marketplace.domain.userprofile.UserProfile;
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

    @Inject
    public UserProfileCommandService(AggregateStoreRepository aggregateStoreRepository) {
        this.aggregateStoreRepository = aggregateStoreRepository;
    }

    public Try<CreateUserProfileResult> handle(CreateUserProfileCommand command) {
        var userId = UserId.newId();
        var displayName = new DisplayName(command.getDisplayName());
        UserProfile userProfile = new UserProfile(userId, command.fullName(), displayName);

        return Try.of(() -> aggregateStoreRepository.add(userProfile))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> ImmutableCreateUserProfileResult.builder()
                .id(user.getId().id())
                .build());
    }

    public CommandHandlerResult<UpdateUserProfileResult> handle(UpdateUserProfileCommand command) {
        return aggregateStoreRepository.load(UserId.from(command.getUserId()))
            .map(it -> (UserProfile) it)
            .flatMap(userProfile -> {
                userProfile.updatePhoto(command.getPhotoUrl());
                return doUserProfileUpdate(command.getUserId(), userProfile);
            })
            .orElse(ImmutableCommandHandlerResult.<UpdateUserProfileResult>builder()
                .result(ImmutableUpdateUserProfileResult.builder()
                    .id(command.getUserId())
                    .build())
                .isSuccessful(false)
                .message("user with id " + command.getUserId().toString() + " was not found to be updated.")
                .build());
    }

    public CommandHandlerResult<UpdateUserProfileResult> handle(UpdateUserFullNameCommand command) {
        return aggregateStoreRepository.load(UserId.from(command.getUserId()))
            .map(it -> (UserProfile) it)
            .flatMap(userProfile -> updateUserFullName(command, userProfile))
            .orElse(ImmutableCommandHandlerResult.<UpdateUserProfileResult>builder()
                .result(ImmutableUpdateUserProfileResult.builder().id(command.getUserId()).build())
                .isSuccessful(false)
                .message("user with id " + command.getUserId().toString() + " was not found to be updated.")
                .build());
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
