package com.marketplace.fixtures;

import com.marketplace.context.ObjectMapperModule;
import com.marketplace.domain.userprofile.controller.CreateUserProfileCommand;
import com.marketplace.domain.userprofile.controller.UpdateUserProfileCommand;

import java.io.IOException;

public class UserProfileFixture {

    public static CreateUserProfileCommand loadCreateUserProfileDto() throws IOException {
        var resourceAsStream = UserProfileFixture.class.getClassLoader().getResourceAsStream("fixtures/create_user_profile.json");
        return ObjectMapperModule.provideObjectMapper().readValue(resourceAsStream, CreateUserProfileCommand.class);
    }

    public static UpdateUserProfileCommand loadUpdateUserProfileDto() throws IOException {
        var resourceAsStream = UserProfileFixture.class.getClassLoader().getResourceAsStream("fixtures/update_user_profile_photo_url.json");
        return ObjectMapperModule.provideObjectMapper().readValue(resourceAsStream, UpdateUserProfileCommand.class);
    }
}
