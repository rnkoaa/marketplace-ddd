package com.marketplace.domain.userprofile.controller;

import javax.inject.Inject;

public class UserProfileController {

    private final UserProfileService userProfileService;

    @Inject
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    public CreateUserProfileResult createUserProfile(CreateUserProfileCommand command) {

        var createResult = userProfileService.handle(command);
        return createResult.result;
    }

    public UpdateUserProfileResult updateUserFullName(UpdateUserFullNameCommand command) {

        var updateResult = userProfileService.handle(command);
        return updateResult.result;
    }

    public UpdateUserProfileResult updateUserDisplayName(UpdateUserDisplayNameCommand command) {
        var updateResult = userProfileService.handle(command);
        return updateResult.result;
    }

    public UpdateUserProfileResult updateUserProfile(UpdateUserProfileCommand command) {

        var updateResult = userProfileService.handle(command);
        return updateResult.result;
    }
}
