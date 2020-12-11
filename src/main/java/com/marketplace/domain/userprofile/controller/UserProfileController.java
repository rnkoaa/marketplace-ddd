package com.marketplace.domain.userprofile.controller;

import java.util.Optional;
import javax.inject.Inject;

public class UserProfileController {

  private final UserProfileService userProfileService;

  @Inject
  public UserProfileController(UserProfileService userProfileService) {
    this.userProfileService = userProfileService;
  }

  public Optional<CreateUserProfileResult> createUserProfile(CreateUserProfileCommand command) {

    var createResult = userProfileService.handle(command);
    return createResult.getResult();
  }

  public Optional<UpdateUserProfileResult> updateUserFullName(UpdateUserFullNameCommand command) {

    var updateResult = userProfileService.handle(command);
    return updateResult.getResult();
  }

  public Optional<UpdateUserProfileResult> updateUserDisplayName(UpdateUserDisplayNameCommand command) {
    var updateResult = userProfileService.handle(command);
    return updateResult.getResult();
  }

  public Optional<UpdateUserProfileResult> updateUserProfile(UpdateUserProfileCommand command) {

    var updateResult = userProfileService.handle(command);
    return updateResult.getResult();
  }
}
