package com.marketplace;

import static com.marketplace.eventstore.jdbc.Tables.EVENT_DATA;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.marketplace.config.ApplicationConfig;
import com.marketplace.config.ConfigLoader;
import com.marketplace.context.ApplicationContext;
import com.marketplace.context.DaggerApplicationContext;
import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.DisplayName;
import com.marketplace.domain.userprofile.UserProfile;
import com.marketplace.domain.userprofile.controller.CreateUserProfileCommand;
import com.marketplace.domain.userprofile.controller.UpdateUserProfileCommand;
import com.marketplace.domain.userprofile.repository.UserProfileCommandRepository;
import com.marketplace.fixtures.UserProfileFixture;
import java.io.IOException;
import java.util.Optional;
import org.jooq.DSLContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserProfileCommandRepositoryTest extends AbstractContainerInitializer {

    String insertId = "0b8a557d-32f6-4268-80d5-6a38df8a9520";
    ApplicationContext context;
    DSLContext dslContext;
    UserProfileCommandRepository userProfileCommandRepository;

    @BeforeEach
    void setup() throws IOException {
        ApplicationConfig config =
            ConfigLoader.loadClasspathResource("application.yml", ApplicationConfig.class);

        context = DaggerApplicationContext.builder().config(config).build();
        dslContext = context.getDSLContext();
        userProfileCommandRepository = context.getUserProfileCommandRepository();
        dslContext.delete(EVENT_DATA).execute();
    }

    @AfterEach
    public void cleanup() {
        dslContext.delete(EVENT_DATA).execute();
    }

    @Test
    void userProfileCanBeCreated() throws IOException {
        CreateUserProfileCommand command = UserProfileFixture.loadCreateUserProfileDto();
        assertThat(command).isNotNull();
        assertThat(command.getFirstName()).isNotBlank();
        assertThat(command.getMiddleName()).isPresent();
        assertThat(command.getLastName()).isNotBlank();

        UserProfile userProfile =
            new UserProfile(UserId.from(insertId), command.fullName(), command.displayName());

        var addedUserProfile = userProfileCommandRepository.add(userProfile);
        assertThat(addedUserProfile).isPresent();
    }

    @Test
    void userProfileCanBeCreatedAndLoaded() throws IOException {
        CreateUserProfileCommand command = UserProfileFixture.loadCreateUserProfileDto();
        assertThat(command).isNotNull();
        assertThat(command.getFirstName()).isNotBlank();
        assertThat(command.getMiddleName()).isPresent();
        assertThat(command.getLastName()).isNotBlank();

        UserProfile userProfile =
            new UserProfile(UserId.from(insertId), command.fullName(), command.displayName());

        var addedUserProfile = userProfileCommandRepository.add(userProfile);
        assertThat(addedUserProfile).isPresent();

        Optional<UserProfile> load = userProfileCommandRepository.load(addedUserProfile.get().getId());
        assertThat(load).isPresent();

        UserProfile savedUserProfile = load.get();

        assertThat(savedUserProfile.getId()).isNotNull();
    }

    @Test
    void userProfileCanBeUpdated() throws IOException {
        CreateUserProfileCommand createUserProfileCmd = UserProfileFixture.loadCreateUserProfileDto();
        UpdateUserProfileCommand updateUserProfileCommand =
            UserProfileFixture.loadUpdateUserProfileDto();
        assertThat(createUserProfileCmd).isNotNull();
        assertThat(createUserProfileCmd.getFirstName()).isNotBlank();
        assertThat(createUserProfileCmd.getMiddleName()).isPresent();
        assertThat(createUserProfileCmd.getMiddleName().get()).isNotBlank();
        assertThat(createUserProfileCmd.getLastName()).isNotBlank();

        UserProfile userProfile =
            new UserProfile(
                UserId.from(insertId),
                createUserProfileCmd.fullName(),
                new DisplayName(createUserProfileCmd.getDisplayName()));

        var addedUserProfile = userProfileCommandRepository.add(userProfile);
        assertThat(addedUserProfile).isPresent();

        var profile = addedUserProfile.get();

        profile.updatePhoto(updateUserProfileCommand.getPhotoUrl());
        var secondSaved = userProfileCommandRepository.add(profile);
        assertThat(secondSaved).isPresent();

        Optional<UserProfile> load = userProfileCommandRepository.load(profile.getId());
        assertThat(load).isPresent();

        UserProfile savedUserProfile = load.get();

        assertThat(savedUserProfile.getId()).isNotNull();
        assertThat(savedUserProfile.getPhotoUrl()).isEqualTo("http://photos.me/user-2.jpg");
        assertThat(savedUserProfile.getVersion()).isEqualTo(2);
    }

    @Test
    void userProfileCanBeCreatedAndShownToExist() throws IOException {
        CreateUserProfileCommand command = UserProfileFixture.loadCreateUserProfileDto();
        assertThat(command).isNotNull();
        assertThat(command.getFirstName()).isNotBlank();
        assertThat(command.getMiddleName()).isPresent();
        assertThat(command.getLastName()).isNotBlank();

        UserProfile userProfile =
            new UserProfile(UserId.from(insertId), command.fullName(), command.displayName());

        var addedUserProfile = userProfileCommandRepository.add(userProfile);
        assertThat(addedUserProfile).isPresent();

        boolean exists = userProfileCommandRepository.exists(addedUserProfile.get().getId());
        assertThat(exists).isTrue();
    }

    @Test
    void testUserProfileCanBeLoadedAndUpdated() throws IOException {
        CreateUserProfileCommand createUserProfileCmd = UserProfileFixture.loadCreateUserProfileDto();
        UpdateUserProfileCommand updateUserProfileCommand =
            UserProfileFixture.loadUpdateUserProfileDto();

        UserProfile userProfile =
            new UserProfile(
                UserId.from(insertId),
                createUserProfileCmd.fullName(),
                new DisplayName(createUserProfileCmd.getDisplayName()));

        var addedUserProfile = userProfileCommandRepository.add(userProfile);
        assertThat(addedUserProfile).isPresent();

        Optional<UserProfile> loadedUserProfile = userProfileCommandRepository.load(userProfile.getId());
        assertThat(loadedUserProfile).isPresent();

        UserProfile profile = loadedUserProfile.get();
        profile.updatePhoto(updateUserProfileCommand.getPhotoUrl());
        var secondSaved = userProfileCommandRepository.add(profile);
        assertThat(secondSaved).isPresent();

        Optional<UserProfile> load = userProfileCommandRepository.load(profile.getId());
        assertThat(load).isPresent();

        UserProfile savedUserProfile = load.get();

        assertThat(savedUserProfile.getId()).isNotNull();
        assertThat(savedUserProfile.getPhotoUrl()).isEqualTo("http://photos.me/user-2.jpg");
        assertThat(savedUserProfile.getVersion()).isEqualTo(2);
    }
}
