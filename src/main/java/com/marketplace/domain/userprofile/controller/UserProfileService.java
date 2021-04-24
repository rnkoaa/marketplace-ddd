package com.marketplace.domain.userprofile.controller;

import com.marketplace.cqrs.command.CommandHandlerResult;
import com.marketplace.cqrs.command.ImmutableCommandHandlerResult;
import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.DisplayName;
import com.marketplace.domain.userprofile.FullName;
import com.marketplace.domain.userprofile.UserProfile;
import com.marketplace.domain.userprofile.repository.UserProfileRepository;

import java.util.Optional;

public class UserProfileService {

  private final UserProfileRepository userProfileRepository;

  public UserProfileService(UserProfileRepository userProfileRepository) {
    this.userProfileRepository = userProfileRepository;
  }

  public CommandHandlerResult<CreateUserProfileResult> handle(CreateUserProfileCommand command) {
    var userId = UserId.newId();
    var fullName = new FullName(command.getFirstName(), command.getMiddleName(), command.getLastName());
    var displayName = new DisplayName(command.getDisplayName());
    UserProfile userProfile = new UserProfile(userId, fullName, displayName);
    UserProfile saved = userProfileRepository.add(userProfile);
    if (saved != null) {
      return ImmutableCommandHandlerResult.<CreateUserProfileResult>builder()
          .result(ImmutableCreateUserProfileResult.builder().id(userId.id()).build())
          .isSuccessful(true)
          .build();
    }
    return ImmutableCommandHandlerResult.<CreateUserProfileResult>builder()
        .isSuccessful(false)
        .message("failed to create user, please try again")
        .build();
  }

  public CommandHandlerResult<UpdateUserProfileResult> handle(UpdateUserProfileCommand command) {
    Optional<UserProfile> load = userProfileRepository.load(UserId.from(command.getUserId()));
    return load
        .map(userProfile -> {
          userProfile.updatePhoto(command.getPhotoUrl());
          UserProfile saved = userProfileRepository.add(userProfile);
          if (saved != null) {
            return ImmutableCommandHandlerResult.<UpdateUserProfileResult>builder()
                .result(ImmutableUpdateUserProfileResult.builder().id(command.getUserId()).build())
                .isSuccessful(true)
                .build();
          }
          return ImmutableCommandHandlerResult.<UpdateUserProfileResult>builder()
              .result(ImmutableUpdateUserProfileResult.builder().id(command.getUserId()).build())
              .isSuccessful(false)
              .message("failed to update user, please try again")
              .build();
        })
        .orElse(ImmutableCommandHandlerResult.<UpdateUserProfileResult>builder()
            .result(ImmutableUpdateUserProfileResult.builder().id(command.getUserId()).build())
            .isSuccessful(false)
            .message("user with id " + command.getUserId().toString() + " was not found to be updated.")
            .build());
  }

  public CommandHandlerResult<UpdateUserProfileResult> handle(UpdateUserFullNameCommand command) {
    Optional<UserProfile> load = userProfileRepository.load(UserId.from(command.getUserId()));
    return load
        .map(userProfile -> {
          var fullName = new FullName(command.getFirstName(), command.getMiddleName(), command.getLastName());
          userProfile.updateUserFullName(fullName);
          UserProfile saved = userProfileRepository.add(userProfile);
          if (saved != null) {
            return ImmutableCommandHandlerResult.<UpdateUserProfileResult>builder()
                .result(ImmutableUpdateUserProfileResult.builder().id(command.getUserId()).build())
                .isSuccessful(true)
                .build();
          }
          return ImmutableCommandHandlerResult.<UpdateUserProfileResult>builder()
              .result(ImmutableUpdateUserProfileResult.builder().id(command.getUserId()).build())
              .isSuccessful(false)
              .message("failed to update user, please try again")
              .build();
        })
        .orElse(ImmutableCommandHandlerResult.<UpdateUserProfileResult>builder()
            .result(ImmutableUpdateUserProfileResult.builder().id(command.getUserId()).build())
            .isSuccessful(false)
            .message("user with id " + command.getUserId().toString() + " was not found to be updated.")
            .build());
  }

  public CommandHandlerResult<UpdateUserProfileResult> handle(UpdateUserDisplayNameCommand command) {
    Optional<UserProfile> load = userProfileRepository.load(UserId.from(command.getUserId()));
    return load
        .map(userProfile -> {
          var displayName = new DisplayName(command.getDisplayName());
          userProfile.updateDisplayName(displayName);
          UserProfile saved = userProfileRepository.add(userProfile);
          if (saved != null) {
            return ImmutableCommandHandlerResult.<UpdateUserProfileResult>builder()
                .result(ImmutableUpdateUserProfileResult.builder().id(command.getUserId()).build())
                .isSuccessful(true)
                .build();
          }
          return ImmutableCommandHandlerResult.<UpdateUserProfileResult>builder()
              .result(ImmutableUpdateUserProfileResult.builder().id(command.getUserId()).build())
              .isSuccessful(false)
              .message("failed to update user, please try again")
              .build();
        })
        .orElse(ImmutableCommandHandlerResult.<UpdateUserProfileResult>builder()
            .result(ImmutableUpdateUserProfileResult.builder().id(command.getUserId()).build())
            .isSuccessful(false)
            .message("user with id " + command.getUserId().toString() + " was not found to be updated.")
            .build());
  }


}
