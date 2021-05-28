package com.marketplace.fixtures;

import com.marketplace.context.ObjectMapperModule;
import com.marketplace.domain.userprofile.controller.ImmutableUpdateUserFullNameCommand;
import com.marketplace.domain.userprofile.controller.UpdateUserFullNameCommand;
import java.io.IOException;
import java.util.UUID;

public class LoadUpdateUserFullName {

    public static UpdateUserFullNameCommand load(UUID userId) throws IOException {
        var resourceAsStream = LoadUpdateAdEvent.class.getClassLoader().getResourceAsStream("fixtures/update_user_full_name.json");
        var updateUserFullNameCommand = ObjectMapperModule.provideObjectMapper()
            .readValue(resourceAsStream, UpdateUserFullNameCommand.class);

        return ImmutableUpdateUserFullNameCommand.copyOf(updateUserFullNameCommand)
            .withUserId(userId);
    }

}
