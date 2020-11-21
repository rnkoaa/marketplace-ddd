package com.marketplace.controller.userprofile;

import javax.inject.Inject;

public class UserProfileController {

    private final UserProfileService userProfileService;

    @Inject
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    public CreateUserProfileResult createUserProfile(CreateUserProfileDto createUserProfileDto) {
        CreateUserProfileCommand command = CreateUserProfileCommand.from(createUserProfileDto);

        var createResult = userProfileService.handle(command);
        return createResult.result;
    }

    public UpdateUserProfileResult updateUserFullName(UpdateUserFullNameDto updateUserFullName) {
        var command = UpdateUserFullNameCommand.from(updateUserFullName);

        var updateResult = userProfileService.handle(command);
        return updateResult.result;
    }

    public UpdateUserProfileResult updateUserDisplayName(UpdateUserDisplayNameDto updateUserDisplayNameDto) {
        var command = UpdateUserDisplayNameCommand.from(updateUserDisplayNameDto);

        var updateResult = userProfileService.handle(command);
        return updateResult.result;
    }

    public UpdateUserProfileResult updateUserProfile(UpdateUserProfileDto updateUserProfileDto) {
        var command = UpdateUserProfileCommand.from(updateUserProfileDto);

        var updateResult = userProfileService.handle(command);
        return updateResult.result;
    }
}
