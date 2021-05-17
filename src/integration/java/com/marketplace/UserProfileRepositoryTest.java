package com.marketplace;

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
import com.marketplace.domain.userprofile.repository.UserProfileRepository;
import com.marketplace.fixtures.UserProfileFixture;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class UserProfileRepositoryTest extends AbstractContainerInitializer {

    String insertId = "0b8a557d-32f6-4268-80d5-6a38df8a9520";
    ApplicationContext context;
    UserProfileRepository userProfileRepository;

    @BeforeEach
    void setup() throws IOException {
        ApplicationConfig config =
            ConfigLoader.loadClasspathResource("application.yml", ApplicationConfig.class);

        context = DaggerApplicationContext.builder().config(config).build();

//    String hosts = mongoDBContainer.getHost();
//    int port = mongoDBContainer.getMappedPort(27017);
//    mongoConfig = new MongoConfig(hosts, "test_db", port);
//    config = ImmutableApplicationConfig.copyOf(config).withMongo(mongoConfig);
//    mongoClient = MongoConfigModule.provideMongoClient(mongoConfig);
        userProfileRepository = context.getUserProfileRepository();
    }

    @AfterEach
    public void cleanup() {
        userProfileRepository.deleteAll();
    }

    @Test
    @Disabled
    void userProfileCanBeCreated() throws IOException {
        CreateUserProfileCommand command = UserProfileFixture.loadCreateUserProfileDto();
        assertThat(command).isNotNull();
        assertThat(command.getFirstName()).isNotBlank();
        assertThat(command.getMiddleName()).isPresent();
        assertThat(command.getLastName()).isNotBlank();

        UserProfile userProfile =
            new UserProfile(UserId.from(insertId), command.fullName(), command.displayName());

        var addedUserProfile = userProfileRepository.add(userProfile);
        assertThat(addedUserProfile).isPresent();
    }

    @Test
    @Disabled
    void userProfileCanBeCreatedAndLoaded() throws IOException {
        CreateUserProfileCommand command = UserProfileFixture.loadCreateUserProfileDto();
        assertThat(command).isNotNull();
        assertThat(command.getFirstName()).isNotBlank();
        assertThat(command.getMiddleName()).isPresent();
        assertThat(command.getLastName()).isNotBlank();

        UserProfile userProfile =
            new UserProfile(UserId.from(insertId), command.fullName(), command.displayName());

        var addedUserProfile = userProfileRepository.add(userProfile);
        assertThat(addedUserProfile).isPresent();

        Optional<UserProfile> load = userProfileRepository.load(addedUserProfile.get().getId());
        assertThat(load).isPresent();

        UserProfile savedUserProfile = load.get();

        assertThat(savedUserProfile.getId()).isNotNull();
        assertThat(savedUserProfile.getChanges()).hasSize(1);
    }

    @Test
    @Disabled
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

        var addedUserProfile = userProfileRepository.add(userProfile);
        assertThat(addedUserProfile).isPresent();

        var profile = addedUserProfile.get();

        profile.updatePhoto(updateUserProfileCommand.getPhotoUrl());
        var secondSaved = userProfileRepository.add(profile);
        assertThat(secondSaved).isPresent();

        Optional<UserProfile> load = userProfileRepository.load(profile.getId());
        assertThat(load).isPresent();

        UserProfile savedUserProfile = load.get();

        assertThat(savedUserProfile.getId()).isNotNull();
        assertThat(savedUserProfile.getChanges()).hasSize(2);
        assertThat(savedUserProfile.getPhotoUrl()).isEqualTo("http://photos.me/user-2.jpg");
    }

    @Test
    @Disabled
    void userProfileCanBeCreatedAndShownToExist() throws IOException {
        CreateUserProfileCommand command = UserProfileFixture.loadCreateUserProfileDto();
        assertThat(command).isNotNull();
        assertThat(command.getFirstName()).isNotBlank();
        assertThat(command.getMiddleName()).isPresent();
        assertThat(command.getLastName()).isNotBlank();

        UserProfile userProfile =
            new UserProfile(UserId.from(insertId), command.fullName(), command.displayName());

        var addedUserProfile = userProfileRepository.add(userProfile);
        assertThat(addedUserProfile).isPresent();

        boolean exists = userProfileRepository.exists(addedUserProfile.get().getId());
        assertThat(exists).isTrue();
    }
}
