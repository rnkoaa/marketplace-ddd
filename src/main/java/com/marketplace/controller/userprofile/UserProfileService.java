package com.marketplace.controller.userprofile;

import com.marketplace.domain.classifiedad.CommandHandlerResult;
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
            return new CommandHandlerResult<>(new CreateUserProfileResult(userId.id()), true, "");
        }
        return new CommandHandlerResult<>(null, false, "failed to create user, please try again");
    }

    public CommandHandlerResult<UpdateUserProfileResult> handle(UpdateUserProfileCommand command) {
        Optional<UserProfile> load = userProfileRepository.load(UserId.from(command.getId()));
        return load
                .map(userProfile -> {
                    userProfile.updatePhoto(command.getPhotoUri());
                    UserProfile saved = userProfileRepository.add(userProfile);
                    if (saved != null) {
                        return new CommandHandlerResult<>(new UpdateUserProfileResult(command.getId()), true, "");
                    }
                    return new CommandHandlerResult<>(new UpdateUserProfileResult(command.getId()), false, "failed to update user");
                })
                .orElse(new CommandHandlerResult<>(new UpdateUserProfileResult(command.getId()), false, "failed to update user"));
    }

    public CommandHandlerResult<UpdateUserProfileResult> handle(UpdateUserFullNameCommand command) {
        Optional<UserProfile> load = userProfileRepository.load(UserId.from(command.getId()));
        return load
                .map(userProfile -> {
                    var fullName = new FullName(command.getFirstName(), command.getMiddleName(), command.getLastName());
                    userProfile.updateUserFullName(fullName);
                    UserProfile saved = userProfileRepository.add(userProfile);
                    if (saved != null) {
                        return new CommandHandlerResult<>(new UpdateUserProfileResult(command.getId()), true, "");
                    }
                    return new CommandHandlerResult<>(new UpdateUserProfileResult(command.getId()), false, "failed to update user");
                })
                .orElse(new CommandHandlerResult<>(new UpdateUserProfileResult(command.getId()), false, "failed to update user"));
    }

    public CommandHandlerResult<UpdateUserProfileResult> handle(UpdateUserDisplayNameCommand command) {
        Optional<UserProfile> load = userProfileRepository.load(UserId.from(command.getId()));
        return load
                .map(userProfile -> {
                    var displayName = new DisplayName(command.getDisplayName());
                    userProfile.updateDisplayName(displayName);
                    UserProfile saved = userProfileRepository.add(userProfile);
                    if (saved != null) {
                        return new CommandHandlerResult<>(new UpdateUserProfileResult(command.getId()), true, "");
                    }
                    return new CommandHandlerResult<>(new UpdateUserProfileResult(command.getId()), false, "failed to update user");
                })
                .orElse(new CommandHandlerResult<>(new UpdateUserProfileResult(command.getId()), false, "failed to update user"));
    }


}
