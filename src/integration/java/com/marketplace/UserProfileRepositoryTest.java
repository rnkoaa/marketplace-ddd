package com.marketplace;

import java.io.IOException;
import org.junit.jupiter.api.Disabled;

@Disabled
public class UserProfileRepositoryTest extends AbstractTestInitializer {

    public UserProfileRepositoryTest() throws IOException {
        super();
    }
//
//  String insertId = "0b8a557d-32f6-4268-80d5-6a38df8a9520";
//  ApplicationContext context;
//  UserProfileRepository userProfileRepository;
//
//  @BeforeEach
//  void setup() throws IOException {
//    ApplicationConfig config =
//        ConfigLoader.loadClasspathResource("application.yml", ApplicationConfig.class);
//
//    context = DaggerApplicationContext.builder().config(config).build();
//
//    String hosts = mongoDBContainer.getHost();
//    int port = mongoDBContainer.getMappedPort(27017);
////    mongoConfig = new MongoConfig(hosts, "test_db", port);
////    config = ImmutableApplicationConfig.copyOf(config).withMongo(mongoConfig);
////    mongoClient = MongoConfigModule.provideMongoClient(mongoConfig);
//    userProfileRepository = context.getUserProfileRepository();
//  }
//
//  @AfterEach
//  public void cleanup() {
//    userProfileRepository.deleteAll();
//  }
//
//  @Test
//  @Disabled
//  void userProfileCanBeCreated() throws IOException {
//    CreateUserProfileCommand command = UserProfileFixture.loadCreateUserProfileDto();
//    assertThat(command).isNotNull();
//    assertThat(command.getFirstName()).isNotBlank();
//    assertThat(command.getMiddleName()).isNotBlank();
//    assertThat(command.getLastName()).isNotBlank();
//
//    UserProfile userProfile =
//        new UserProfile(UserId.from(insertId), command.fullName(), command.displayName());
//
//    UserProfile add = userProfileRepository.add(userProfile);
//
//    assertThat(add).isNotNull();
//  }
//
//  @Test
//  @Disabled
//  void userProfileCanBeCreatedAndLoaded() throws IOException {
//    CreateUserProfileCommand command = UserProfileFixture.loadCreateUserProfileDto();
//    assertThat(command).isNotNull();
//    assertThat(command.getFirstName()).isNotBlank();
//    assertThat(command.getMiddleName()).isNotBlank();
//    assertThat(command.getLastName()).isNotBlank();
//
//    UserProfile userProfile =
//        new UserProfile(UserId.from(insertId), command.fullName(), command.displayName());
//
//    UserProfile add = userProfileRepository.add(userProfile);
//
//    assertThat(add).isNotNull();
//
//    Optional<UserProfile> load = userProfileRepository.load(add.getId());
//    assertThat(load).isPresent();
//
//    UserProfile savedUserProfile = load.get();
//
//    assertThat(savedUserProfile.getId()).isNotNull();
//    assertThat(savedUserProfile.getChanges()).hasSize(1);
//  }
//
//  @Test
//  @Disabled
//  void userProfileCanBeUpdated() throws IOException {
//    CreateUserProfileCommand createUserProfileCmd = UserProfileFixture.loadCreateUserProfileDto();
//    UpdateUserProfileCommand updateUserProfileCommand =
//        UserProfileFixture.loadUpdateUserProfileDto();
//    assertThat(createUserProfileCmd).isNotNull();
//    assertThat(createUserProfileCmd.getFirstName()).isNotBlank();
//    assertThat(createUserProfileCmd.getMiddleName()).isNotBlank();
//    assertThat(createUserProfileCmd.getLastName()).isNotBlank();
//
//    UserProfile userProfile =
//        new UserProfile(
//            UserId.from(insertId),
//            createUserProfileCmd.fullName(),
//            new DisplayName(createUserProfileCmd.getDisplayName()));
//
//    UserProfile add = userProfileRepository.add(userProfile);
//
//    assertThat(add).isNotNull();
//
//    add.updatePhoto(updateUserProfileCommand.getPhotoUrl());
//    UserProfile secondSaved = userProfileRepository.add(add);
//    assertThat(secondSaved).isNotNull();
//
//    Optional<UserProfile> load = userProfileRepository.load(add.getId());
//    assertThat(load).isPresent();
//
//    UserProfile savedUserProfile = load.get();
//
//    assertThat(savedUserProfile.getId()).isNotNull();
//    assertThat(savedUserProfile.getChanges()).hasSize(2);
//    assertThat(savedUserProfile.getPhotoUrl()).isEqualTo("http://photos.me/user-2.jpg");
//  }
//
//  @Test
//  @Disabled
//  void userProfileCanBeCreatedAndShownToExist() throws IOException {
//    CreateUserProfileCommand command = UserProfileFixture.loadCreateUserProfileDto();
//    assertThat(command).isNotNull();
//    assertThat(command.getFirstName()).isNotBlank();
//    assertThat(command.getMiddleName()).isNotBlank();
//    assertThat(command.getLastName()).isNotBlank();
//
//    UserProfile userProfile =
//        new UserProfile(UserId.from(insertId), command.fullName(), command.displayName());
//
//    UserProfile add = userProfileRepository.add(userProfile);
//
//    assertThat(add).isNotNull();
//
//    boolean exists = userProfileRepository.exists(add.getId());
//    assertThat(exists).isTrue();
//  }
}
