package com.marketplace.entity.userprofile;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.marketplace.BaseRepositoryTest;
import com.marketplace.domain.userprofile.entity.UserProfileEntity;
import com.marketplace.domain.userprofile.event.ImmutableProfilePhotoUploaded;
import com.marketplace.domain.userprofile.event.ImmutableUserDisplayNameUpdated;
import com.marketplace.domain.userprofile.event.ImmutableUserFullNameUpdated;
import com.marketplace.domain.userprofile.event.ImmutableUserRegistered;
import com.marketplace.domain.userprofile.event.ProfilePhotoUploaded;
import com.marketplace.domain.userprofile.event.UserDisplayNameUpdated;
import com.marketplace.domain.userprofile.event.UserFullNameUpdated;
import com.marketplace.domain.userprofile.event.UserRegistered;
import com.marketplace.domain.userprofile.repository.UserProfileQueryRepository;
import java.util.Optional;
import java.util.UUID;
import org.jooq.exception.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserProfileEventListenerIntegrationTest extends BaseRepositoryTest {

    final UUID userId = UUID.fromString("081b772e-c3c8-4d3a-bbcf-59e526daa4c5");
    final UUID eventId = UUID.fromString("681f96b5-4d81-4b8c-91c8-9bacdcefc7af");

    UserProfileQueryRepository userProfileQueryRepository;

    @BeforeEach
    void setupLocal() {
        userProfileQueryRepository = getApplicationContext().getUserProfileQueryRepository();
    }

    @Test
    void testUpdateDisplayNameForExistingUser() {
        seedAndValidateData();

        UserDisplayNameUpdated userDisplayNameUpdated = ImmutableUserDisplayNameUpdated.builder()
            .id(eventId)
            .userId(userId)
            .aggregateId(userId)
            .displayName("tuccigorkaiwovi")
            .build();

        userProfileEventListener.onDisplayNameUpdated(userDisplayNameUpdated);
        Optional<UserProfileEntity> maybeUserProfile = userProfileQueryRepository.findById(userId);
        assertThat(maybeUserProfile).isPresent();

        UserProfileEntity entity = maybeUserProfile.get();
        assertThat(entity.getDisplayName()).isEqualTo("tuccigorkaiwovi");
    }

    @Test
    void testUpdatePhotoUrlForExistingUser() {
        seedAndValidateData();

        ProfilePhotoUploaded profilePhotoUploaded = ImmutableProfilePhotoUploaded.builder()
            .id(eventId)
            .userId(userId)
            .aggregateId(userId)
            .photoUrl("http://aka.ms/1.jpg")
            .build();

        userProfileEventListener.onPhotoAdded(profilePhotoUploaded);
        Optional<UserProfileEntity> maybeUserProfile = userProfileQueryRepository.findById(userId);
        assertThat(maybeUserProfile).isPresent();

        UserProfileEntity entity = maybeUserProfile.get();
        assertThat(entity.getPhotoUrl())
            .isPresent()
            .isEqualTo(Optional.of("http://aka.ms/1.jpg"));
    }

    @Test
    void testUpdateFullNameForExistingUser() {
        seedAndValidateData();

        UserFullNameUpdated userFullNameUpdated = ImmutableUserFullNameUpdated.builder()
            .id(eventId)
            .userId(userId)
            .aggregateId(userId)
            .firstName("tucci")
            .lastName("Gorka")
            .build();

        userProfileEventListener.onUserFullNameUpdated(userFullNameUpdated);
        Optional<UserProfileEntity> maybeUserProfile = userProfileQueryRepository.findById(userId);
        assertThat(maybeUserProfile).isPresent();

        UserProfileEntity entity = maybeUserProfile.get();
        assertThat(entity.getDisplayName()).isEqualTo("ellenosei");
        assertThat(entity.getFirstName()).isEqualTo("tucci");
        assertThat(entity.getLastName()).isEqualTo("Gorka");
    }

    @Test
    void testNewEntityCanBeCreatedFromRegisteredEvent() {
        seedAndValidateData();

        Optional<UserProfileEntity> userProfileById = userProfileQueryRepository.findById(userId);
        assertThat(userProfileById).isPresent();

        UserProfileEntity entity = userProfileById.get();
        assertThat(entity.getDisplayName()).isEqualTo("ellenosei");
        assertThat(entity.getFirstName()).isEqualTo("Ellen");
        assertThat(entity.getLastName()).isEqualTo("Osei");
        assertThat(entity.getMiddleName()).isEqualTo("Rosebud");
    }

    @Test
    void testUserWithSameDisplayNameCannotBeRegisteredTwice() {
        seedAndValidateData();

        var secondUserId = UUID.fromString("4bdd3fd7-dfd9-46b8-a063-826f2e6b807f");
        UserRegistered newRegisteredUser = ImmutableUserRegistered.builder()
            .id(eventId)
            .userId(secondUserId)
            .aggregateId(secondUserId)
            .firstName("Ellen")
            .lastName("Osei")
            .middleName("Rosebud")
            .displayName("ellenosei")
            .build();

        Assertions.assertThrows(DataAccessException.class, () -> {
            userProfileEventListener.onUserCreated(newRegisteredUser);
        });
        Optional<UserProfileEntity> secondFindById = userProfileQueryRepository.findById(secondUserId);
        assertThat(secondFindById).isNotPresent();
    }

    @Test
    void sameUserCannotBeRegisteredTwiceViaSameId() {
        UserRegistered userRegisteredEvt = seedAndValidateData();
        userProfileEventListener.onUserCreated(userRegisteredEvt);
    }

    UserRegistered seedAndValidateData() {
        UserRegistered userRegisteredEvt = ImmutableUserRegistered.builder()
            .id(eventId)
            .userId(userId)
            .aggregateId(userId)
            .firstName("Ellen")
            .lastName("Osei")
            .middleName("Rosebud")
            .displayName("ellenosei")
            .build();

        userProfileEventListener.onUserCreated(userRegisteredEvt);

        assertThat(userProfileQueryRepository.findById(userId)).isPresent();

        return userRegisteredEvt;
    }
}
