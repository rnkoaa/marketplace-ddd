package com.marketplace.entity.userprofile;

import com.marketplace.BaseRepositoryTest;

public class UserProfileQueryRepositoryTest extends BaseRepositoryTest {

//    UserProfileCommandRepository userProfileCommandRepository;
//
//    @BeforeEach
//    void setup() {
//        userProfileCommandRepository = getApplicationContext().getUserProfileCommandRepository();
//    }
//
//    @Test
//    void userProfileCanBeSaved() {
//        var userProfile = new UserProfile(
//            UserId.newId(),
//            new FullName("Hello", "", "World"),
//            new DisplayName("helloworld")
//        );
//
//        var addedUserProfile = userProfileQueryEntityRepository.add(userProfile);
//        assertThat(addedUserProfile).isPresent();
//    }
//
//    @Test
//    void testLoadUserProfile() {
//        var userProfile = new UserProfile(
//            UserId.newId(),
//            new FullName("Hello", "", "World"),
//            new DisplayName("helloworld")
//        );
//
//        var addedUserProfile = userProfileQueryEntityRepository.add(userProfile);
//        assertThat(addedUserProfile).isPresent();
//
//        var maybeUser = userProfileQueryEntityRepository.load(userProfile.getId());
//        assertThat(maybeUser).isPresent();
//
//        UserProfile savedUserProfile = maybeUser.get();
//        assertThat(savedUserProfile.getId()).isEqualTo(userProfile.getId());
//        assertThat(savedUserProfile.getDisplayName()).isEqualTo(userProfile.getDisplayName());
//        assertThat(savedUserProfile.getFullName()).isEqualTo(userProfile.getFullName());
//    }
//
//    @Test
//    void updateExistingUserProfile() {
//        var userProfile = new UserProfile(
//            UserId.newId(),
//            new FullName("Hello", "", "World"),
//            new DisplayName("helloworld")
//        );
//
//        var addedUserProfile = userProfileQueryEntityRepository.add(userProfile);
//        assertThat(addedUserProfile).isPresent();
//
//        var secondUserProfile = new UserProfile(userProfile.getId(),
//            userProfile.getFullName(), new DisplayName("helloworld2"));
//
//        var secondSave = userProfileQueryEntityRepository.add(secondUserProfile);
//        assertThat(secondSave).isNotNull();
//
//        Optional<UserProfile> foundUserProfile = userProfileQueryEntityRepository.load(userProfile.getId());
//        assertThat(foundUserProfile).isPresent();
//
//        UserProfile userProfile1 = foundUserProfile.get();
//        assertThat(userProfile1.getDisplayName()).isEqualTo(secondUserProfile.getDisplayName());
//    }
//
//    @Test
//    void testUserProfileExists() {
//        var userProfile = new UserProfile(
//            UserId.newId(),
//            new FullName("Hello", "", "World"),
//            new DisplayName("helloworld")
//        );
//
//        var addedUserProfile = userProfileQueryEntityRepository.add(userProfile);
//        assertThat(addedUserProfile).isPresent();
//
//        boolean exists = userProfileQueryEntityRepository.exists(userProfile.getId());
//        assertThat(exists).isTrue();
//    }
//
//    @Test
//    void testUserProfileDoesNotExist() {
//        boolean exists = userProfileQueryEntityRepository.exists(UserId.newId());
//        assertThat(exists).isFalse();
//    }


}
