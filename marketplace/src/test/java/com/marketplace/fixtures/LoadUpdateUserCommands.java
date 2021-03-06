package com.marketplace.fixtures;

import com.marketplace.context.ObjectMapperModule;
import com.marketplace.domain.userprofile.controller.command.ImmutableUpdateUserDisplayNameCommand;
import com.marketplace.domain.userprofile.controller.command.ImmutableUpdateUserFullNameCommand;
import com.marketplace.domain.userprofile.controller.command.ImmutableUpdateUserProfileCommand;
import com.marketplace.domain.userprofile.controller.command.UpdateUserDisplayNameCommand;
import com.marketplace.domain.userprofile.controller.command.UpdateUserFullNameCommand;
import com.marketplace.domain.userprofile.controller.command.UpdateUserProfileCommand;
import java.io.IOException;
import java.util.UUID;

public class LoadUpdateUserCommands {

    public static UpdateUserFullNameCommand load(UUID userId) throws IOException {
        var resourceAsStream = LoadUpdateAdEvent.class.getClassLoader()
            .getResourceAsStream("fixtures/update_user_full_name.json");
        var updateUserFullNameCommand = ObjectMapperModule.provideObjectMapper()
            .readValue(resourceAsStream, UpdateUserFullNameCommand.class);

        return ImmutableUpdateUserFullNameCommand.copyOf(updateUserFullNameCommand)
            .withUserId(userId);
    }

    public static UpdateUserDisplayNameCommand loadDisplayUpdate(UUID userId) throws IOException {
        var resourceAsStream = LoadUpdateAdEvent.class.getClassLoader()
            .getResourceAsStream("fixtures/update_user_display_name.json");
        var updateUserFullNameCommand = ObjectMapperModule.provideObjectMapper()
            .readValue(resourceAsStream, UpdateUserDisplayNameCommand.class);

        return ImmutableUpdateUserDisplayNameCommand.copyOf(updateUserFullNameCommand)
            .withUserId(userId);
    }

    public static UpdateUserProfileCommand loadUpdateUserProfile(UUID userId) throws IOException {
        var resourceAsStream = LoadUpdateAdEvent.class.getClassLoader()
            .getResourceAsStream("fixtures/update_user_profile.json");
        var updateUserProfile = ObjectMapperModule.provideObjectMapper()
            .readValue(resourceAsStream, UpdateUserProfileCommand.class);

        return ImmutableUpdateUserProfileCommand.copyOf(updateUserProfile)
            .withUserId(userId);
    }

}
