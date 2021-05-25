package com.marketplace.domain.userprofile.controller;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.marketplace.domain.AggregateStoreRepository;
import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.UserProfile;
import io.vavr.control.Try;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserProfileCommandServiceTest {

    //
    @Mock
    AggregateStoreRepository aggregateStoreRepository;
    private UserProfileCommandService userProfileCommandService;

    @BeforeEach
    void setup() {
        userProfileCommandService = new UserProfileCommandService(aggregateStoreRepository);
    }

    @Test
    void testUserProfileCanBeCreated() {
        var expectedUserId = UUID.fromString("ddfa7983-e2ff-413c-937b-1e6aa800f6d8");
        var createCommand = ImmutableCreateUserProfileCommand.builder()
            .firstName("Helen")
            .lastName("Osei")
            .middleName("Goka")
            .displayName("tucci")
            .build();
//
        var expectedUserProfile = new UserProfile(UserId.from(expectedUserId), createCommand.fullName(),
            createCommand.displayName());

        Mockito.when(aggregateStoreRepository.add(Mockito.any(UserProfile.class)))
            .thenReturn(Optional.of(expectedUserProfile));
        Try<CreateUserProfileResult> handle = userProfileCommandService.handle(createCommand);
        assertThat(handle).isNotNull();

        assertThat(handle.isSuccess()).isTrue();
        Optional<CreateUserProfileResult> result = handle.toJavaOptional();
        assertThat(result).isPresent();
        CreateUserProfileResult createUserProfileResult = result.get();

        assertThat(createUserProfileResult.getId()).isEqualTo(expectedUserId);
    }
//
//    @Test
//    void testFailedCreateWillFailCreatingUser() {
//        var createCommand = ImmutableCreateUserProfileCommand.builder()
//            .firstName("Helen")
//            .lastName("Osei")
//            .middleName("Goka")
//            .displayName("tucci")
//            .build();
//
//        Mockito.when(aggregateStoreRepository.add(Mockito.any(UserProfile.class)))
//            .thenReturn(Optional.empty());
//        Try<CreateUserProfileResult> handle = userProfileCommandService.handle(createCommand);
//        assertThat(handle).isNotNull();
//
//        assertThat(handle.isSuccess()).isFalse();
//        assertThat(handle.toJavaOptional()).isNotPresent();
////        assertThat(handle.getMessage()).isPresent()
////            .get()
////            .isEqualTo("failed to create user, please try again");
//    }
//
//    @Test
//    void testUserProfileCanBeUpdated() {
//        var expectedUserId = UUID.fromString("ddfa7983-e2ff-413c-937b-1e6aa800f6d8");
//        var createCommand = ImmutableUpdateUserProfileCommand.builder()
//            .userId(expectedUserId)
//            .photoUrl("https://tucciivowi.files.wordpress.com/2019/06/tucci-3.jpg?w=1680")
//            .build();
//
//        var fullName = new FullName("Tucci", "Gokka", "Ivowi");
//
//        var expectedUserProfile = new UserProfile(UserId.from(expectedUserId), fullName, new DisplayName("tucciivowi"));
//
//        Mockito.when(aggregateStoreRepository.add(Mockito.any(UserProfile.class)))
//            .thenReturn(Optional.of(expectedUserProfile));
//
//        Mockito.when(aggregateStoreRepository.load(expectedUserProfile.getId()))
//            .thenReturn(Optional.of(expectedUserProfile));
//
//        CommandHandlerResult<UpdateUserProfileResult> handle = userProfileCommandService.handle(createCommand);
//        assertThat(handle).isNotNull();
//
//        assertThat(handle.isSuccessful()).isTrue();
//        Optional<UpdateUserProfileResult> result = handle.getResult();
//        assertThat(result).isPresent();
//        UpdateUserProfileResult updateUserProfileResult = result.get();
//
//        assertThat(updateUserProfileResult.getId()).isEqualTo(expectedUserId);
//    }
//
//    @Test
//    void testCannotUpdateMissingUserProfile() {
//        var expectedUserId = UUID.fromString("ddfa7983-e2ff-413c-937b-1e6aa800f6d8");
//        var updateCommand = ImmutableUpdateUserProfileCommand.builder()
//            .userId(expectedUserId)
//            .photoUrl("https://tucciivowi.files.wordpress.com/2019/06/tucci-3.jpg?w=1680")
//            .build();
//
//        Mockito.when(aggregateStoreRepository.load(UserId.from(expectedUserId)))
//            .thenReturn(Optional.empty());
//
//        CommandHandlerResult<UpdateUserProfileResult> handle = userProfileCommandService.handle(updateCommand);
//        assertThat(handle).isNotNull();
//
//        assertThat(handle.isSuccessful()).isFalse();
//        assertThat(handle.getMessage()).isNotEmpty()
//            .isEqualTo(Optional.of("user with id ddfa7983-e2ff-413c-937b-1e6aa800f6d8 was not found to be updated."));
//        Optional<UpdateUserProfileResult> result = handle.getResult();
//        assertThat(result).isPresent();
//    }
//
//    @Test
//    void testAnyFailureUpdatesWillReturnEmpty() {
//        var expectedUserId = UUID.fromString("ddfa7983-e2ff-413c-937b-1e6aa800f6d8");
//        var updateCommand = ImmutableUpdateUserProfileCommand.builder()
//            .userId(expectedUserId)
//            .photoUrl("https://tucciivowi.files.wordpress.com/2019/06/tucci-3.jpg?w=1680")
//            .build();
//        var fullName = new FullName("Tucci", "Gokka", "Ivowi");
//
//        AggregateRoot<EventId, VersionedEvent> expectedUserProfile = new UserProfile(UserId.from(expectedUserId),
//            fullName, new DisplayName("tucciivowi"));
//
//        Mockito.when(aggregateStoreRepository.add(Mockito.any(UserProfile.class)))
//            .thenReturn(Optional.empty());
//
//        Mockito.when(aggregateStoreRepository.load(UserId.from(expectedUserId)))
//            .thenReturn(Optional.of(expectedUserProfile));
//
//        CommandHandlerResult<UpdateUserProfileResult> handle = userProfileCommandService.handle(updateCommand);
//        assertThat(handle).isNotNull();
//
//        assertThat(handle.isSuccessful()).isFalse();
//        assertThat(handle.getMessage()).isNotEmpty()
//            .isEqualTo(Optional.of("failed to update user, please try again"));
//        Optional<UpdateUserProfileResult> result = handle.getResult();
//        assertThat(result).isPresent();
//    }
//
//    @Test
//    void testUserProfileFullNameCanBeUpdated() {
//        var expectedUserId = UUID.fromString("ddfa7983-e2ff-413c-937b-1e6aa800f6d8");
//        var updateCommand = ImmutableUpdateUserFullNameCommand.builder()
//            .userId(expectedUserId)
//            .firstName("Tucci")
//            .lastName("Ivowi")
//            .build();
//
//        var fullName = new FullName("Tucci", "Gokka", "Ivowi");
//
//        var expectedUserProfile = new UserProfile(UserId.from(expectedUserId), fullName, new DisplayName("tucciivowi"));
//
//        Mockito.when(aggregateStoreRepository.add(Mockito.any(UserProfile.class)))
//            .thenReturn(Optional.of(expectedUserProfile));
//
//        Mockito.when(aggregateStoreRepository.load(expectedUserProfile.getId()))
//            .thenReturn(Optional.of(expectedUserProfile));
//
//        CommandHandlerResult<UpdateUserProfileResult> handle = userProfileCommandService.handle(updateCommand);
//        assertThat(handle).isNotNull();
//
//        assertThat(handle.isSuccessful()).isTrue();
//        Optional<UpdateUserProfileResult> result = handle.getResult();
//        assertThat(result).isPresent();
//        UpdateUserProfileResult updateUserProfileResult = result.get();
//
//        assertThat(updateUserProfileResult.getId()).isEqualTo(expectedUserId);
//    }
//
//    @Test
//    void testCannotUpdateFullNameMissingUserProfile() {
//        var expectedUserId = UUID.fromString("ddfa7983-e2ff-413c-937b-1e6aa800f6d8");
//        var updateCommand = ImmutableUpdateUserFullNameCommand.builder()
//            .userId(expectedUserId)
//            .firstName("Tucci")
//            .lastName("Ivowi")
//            .build();
//
//        Mockito.when(aggregateStoreRepository.load(UserId.from(expectedUserId)))
//            .thenReturn(Optional.empty());
//
//        CommandHandlerResult<UpdateUserProfileResult> handle = userProfileCommandService.handle(updateCommand);
//        assertThat(handle).isNotNull();
//
//        assertThat(handle.isSuccessful()).isFalse();
//        assertThat(handle.getMessage()).isNotEmpty()
//            .isEqualTo(Optional.of("user with id ddfa7983-e2ff-413c-937b-1e6aa800f6d8 was not found to be updated."));
//        Optional<UpdateUserProfileResult> result = handle.getResult();
//        assertThat(result).isPresent();
//    }
//
//    @Test
//    void testAnyFailureFullNameUpdatesWillReturnEmpty() {
//        var expectedUserId = UUID.fromString("ddfa7983-e2ff-413c-937b-1e6aa800f6d8");
//        var updateCommand = ImmutableUpdateUserFullNameCommand.builder()
//            .userId(expectedUserId)
//            .firstName("Tucci")
//            .lastName("Ivowi")
//            .build();
//        var fullName = new FullName("Tucci", "Gokka", "Ivowi");
//
//        var expectedUserProfile = new UserProfile(UserId.from(expectedUserId), fullName, new DisplayName("tucciivowi"));
//
//        Mockito.when(aggregateStoreRepository.add(Mockito.any(UserProfile.class)))
//            .thenReturn(Optional.empty());
//
//        Mockito.when(aggregateStoreRepository.load(UserId.from(expectedUserId)))
//            .thenReturn(Optional.of(expectedUserProfile));
//
//        CommandHandlerResult<UpdateUserProfileResult> handle = userProfileCommandService.handle(updateCommand);
//        assertThat(handle).isNotNull();
//
//        assertThat(handle.isSuccessful()).isFalse();
//        assertThat(handle.getMessage()).isNotEmpty()
//            .isEqualTo(Optional.of("failed to update user, please try again"));
//        Optional<UpdateUserProfileResult> result = handle.getResult();
//        assertThat(result).isPresent();
//    }

}