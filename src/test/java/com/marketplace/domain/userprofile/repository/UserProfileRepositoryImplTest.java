package com.marketplace.domain.userprofile.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserProfileRepositoryImplTest {
    private final String userIdStr = "4fdd7238-9f90-407b-a863-c423a2090c97";

//    private final MongoTemplate mongoTemplate = Mockito.mock(MongoTemplate.class);

    private UserProfileRepository userProfileRepository;

    @BeforeEach
    public void setup() {
//        userProfileRepository = new UserProfileRepositoryImpl(mongoTemplate);
    }

    @Test
    void addUserProfile() {
//        var userProfile = new UserProfile(UserId.fromString(userIdStr),
//                new FullName("Hello", "A", "world"),
//                new DisplayName("user"));
//        UserProfileEntity entity = UserProfileEntity.from(userProfile);
//        Mockito.when(mongoTemplate.add(any(UserProfileEntity.class), any(), any(String.class), any()))
//                .thenReturn(entity);
//        UserProfile savedUserProfile = userProfileRepository.add(userProfile);
//
//        assertThat(savedUserProfile).isNotNull();
    }

    @Test
    void profileCanBeSavedAndLoaded() {
//        var userProfile = new UserProfile(UserId.fromString(userIdStr),
//                new FullName("Hello", "A", "world"),
//                new DisplayName("user"));
//        UserProfileEntity entity = UserProfileEntity.from(userProfile);
//        Mockito.when(mongoTemplate.add(any(UserProfileEntity.class), any(), any(String.class), any()))
//                .thenReturn(entity);
//        Mockito.when(mongoTemplate.findById(any(), any(String.class), any()))
//                .thenReturn(Optional.of(entity));
//        UserProfile savedUserProfile = userProfileRepository.add(userProfile);
//
//        assertThat(savedUserProfile).isNotNull();
//
//        Optional<UserProfile> load = userProfileRepository.load(UserId.fromString(userIdStr));
//
//        assertThat(load).isPresent();
//        UserProfile foundUserProfile = load.get();
//        assertThat(foundUserProfile.getChanges()).hasSize(1);
    }

}