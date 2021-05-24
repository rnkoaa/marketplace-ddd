package com.marketplace.domain.userprofile;

import com.marketplace.domain.shared.UserId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class UserProfileTest {
    String userIdStr = "4fdd7238-9f90-407b-a863-c423a2090c97";

    @Test
    void createUserProfileResultsInOneEvent() {
        var userProfile = new UserProfile(UserId.from(userIdStr),
                new FullName("Hello", "A", "world"),
                new DisplayName("user"));

        assertThat(userProfile.getChanges()).hasSize(1);
    }

    @Test
    void fullNameCanBeUpdated() {
        var userProfile = new UserProfile(UserId.from(userIdStr),
                new FullName("Hello", "A", "world"),
                new DisplayName("user"));

        assertThat(userProfile.getChanges()).hasSize(1);

        userProfile.updateUserFullName(new FullName("Hello", "B", "World-2"));
        assertThat(userProfile.getChanges()).hasSize(2);
    }

    @Test
    void displayNameCanBeUpdated() {
        var userProfile = new UserProfile(UserId.from(userIdStr),
                new FullName("Hello", "A", "world"),
                new DisplayName("user"));

        assertThat(userProfile.getChanges()).hasSize(1);

        userProfile.updateDisplayName(new DisplayName("user1"));
        assertThat(userProfile.getChanges()).hasSize(2);
    }
    @Test
    void photoUrlCanBeUpdated() {
        var userProfile = new UserProfile(UserId.from(userIdStr),
                new FullName("Hello", "A", "world"),
                new DisplayName("user"));

        assertThat(userProfile.getChanges()).hasSize(1);

        userProfile.updatePhoto("http://me.com/photo.jpg");
        assertThat(userProfile.getChanges()).hasSize(2);
    }

    @Test
    void allEvents() {
        var userProfile = new UserProfile(UserId.from(userIdStr),
                new FullName("Hello", "A", "world"),
                new DisplayName("user"));

        assertThat(userProfile.getChanges()).hasSize(1);

        userProfile.updateUserFullName(new FullName("Hello", "B", "World-2"));
        userProfile.updatePhoto("http://me.com/photo.jpg");
        userProfile.updateDisplayName(new DisplayName("user1"));
        assertThat(userProfile.getChanges()).hasSize(4);
//        userProfile.get
    }

}