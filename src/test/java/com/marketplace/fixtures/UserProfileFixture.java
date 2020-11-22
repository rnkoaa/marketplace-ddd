package com.marketplace.fixtures;

import com.marketplace.context.ObjectMapperModule;
import com.marketplace.controller.classifiedad.UpdateAdDto;
import com.marketplace.controller.userprofile.CreateUserProfileDto;
import com.marketplace.controller.userprofile.UpdateUserProfileDto;

import java.io.IOException;

public class UserProfileFixture {

    public static CreateUserProfileDto loadCreateUserProfileDto() throws IOException {
        var resourceAsStream = UserProfileFixture.class.getClassLoader().getResourceAsStream("fixtures/create_user_profile.json");
        return ObjectMapperModule.provideObjectMapper().readValue(resourceAsStream, CreateUserProfileDto.class);
    }

    public static UpdateUserProfileDto loadUpdateUserProfileDto() throws IOException {
        var resourceAsStream = UserProfileFixture.class.getClassLoader().getResourceAsStream("fixtures/update_user_profile_photo_url.json");
        return ObjectMapperModule.provideObjectMapper().readValue(resourceAsStream, UpdateUserProfileDto.class);
    }
}
