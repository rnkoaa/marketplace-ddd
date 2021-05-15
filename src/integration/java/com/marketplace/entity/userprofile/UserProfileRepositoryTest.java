package com.marketplace.entity.userprofile;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.marketplace.BaseRepositoryTest;
import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.DisplayName;
import com.marketplace.domain.userprofile.FullName;
import com.marketplace.domain.userprofile.UserProfile;
import com.marketplace.domain.userprofile.repository.UserProfileRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserProfileRepositoryTest extends BaseRepositoryTest {

    UserProfileRepository userProfileRepository;

    @BeforeEach
    void setup() {
        userProfileRepository = getApplicationContext().getUserProfileRepository();
    }

    @Test
    void userProfileCanBeSaved() {
        var userProfile = new UserProfile(
            UserId.newId(),
            new FullName("Hello", "", "World"),
            new DisplayName("helloworld")
        );

        UserProfile add = userProfileRepository.add(userProfile);
        assertThat(add).isNotNull();
    }

    @Test
    void testLoadUserProfile() {
        var userProfile = new UserProfile(
            UserId.newId(),
            new FullName("Hello", "", "World"),
            new DisplayName("helloworld")
        );

        UserProfile add = userProfileRepository.add(userProfile);
        assertThat(add).isNotNull();

        var maybeUser = userProfileRepository.load(userProfile.getId());
        assertThat(maybeUser).isPresent();

        UserProfile savedUserProfile = maybeUser.get();
        assertThat(savedUserProfile.getId()).isEqualTo(userProfile.getId());
        assertThat(savedUserProfile.getDisplayName()).isEqualTo(userProfile.getDisplayName());
        assertThat(savedUserProfile.getFullName()).isEqualTo(userProfile.getFullName());
    }

    @Test
    void updateExistingUserProfile() {
        var userProfile = new UserProfile(
            UserId.newId(),
            new FullName("Hello", "", "World"),
            new DisplayName("helloworld")
        );

        UserProfile add = userProfileRepository.add(userProfile);
        assertThat(add).isNotNull();

        var secondUserProfile = new UserProfile(userProfile.getId(),
            userProfile.getFullName(), new DisplayName("helloworld2"));

        var secondSave = userProfileRepository.add(secondUserProfile);
        assertThat(secondSave).isNotNull();

        Optional<UserProfile> foundUserProfile = userProfileRepository.load(userProfile.getId());
        assertThat(foundUserProfile).isPresent();

        UserProfile userProfile1 = foundUserProfile.get();
        assertThat(userProfile1.getDisplayName()).isEqualTo(secondUserProfile.getDisplayName());
    }

    @Test
    void testUserProfileExists() {
        var userProfile = new UserProfile(
            UserId.newId(),
            new FullName("Hello", "", "World"),
            new DisplayName("helloworld")
        );

        UserProfile add = userProfileRepository.add(userProfile);
        assertThat(add).isNotNull();

        boolean exists = userProfileRepository.exists(userProfile.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void testUserProfileDoesNotExist() {
        boolean exists = userProfileRepository.exists(UserId.newId());
        assertThat(exists).isFalse();
    }


}
