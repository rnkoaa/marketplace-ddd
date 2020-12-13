package com.marketplace.domain.userprofile.repository;

import com.marketplace.domain.repository.MongoTemplate;
import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.DisplayName;
import com.marketplace.domain.userprofile.FullName;
import com.marketplace.domain.userprofile.UserProfile;
import com.marketplace.domain.userprofile.entity.UserProfileEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

class UserProfileRepositoryImplTest {
    private final String userIdStr = "4fdd7238-9f90-407b-a863-c423a2090c97";

    private final MongoTemplate mongoTemplate = Mockito.mock(MongoTemplate.class);

    private UserProfileRepository userProfileRepository;

    @BeforeEach
    public void setup() {
        userProfileRepository = new UserProfileRepositoryImpl(mongoTemplate);
    }

    @Test
    void addUserProfile() {
        var userProfile = new UserProfile(UserId.fromString(userIdStr),
                new FullName("Hello", "A", "world"),
                new DisplayName("user"));
        UserProfileEntity entity = UserProfileEntity.from(userProfile);
        Mockito.when(mongoTemplate.add(any(UserProfileEntity.class), any(), any(String.class), any()))
            .thenReturn(Mono.just(entity));
        UserProfile savedUserProfile = userProfileRepository.add(userProfile);

        assertThat(savedUserProfile).isNotNull();
    }

    @Test
    void profileCanBeSavedAndLoaded() {
        var userProfile = new UserProfile(UserId.fromString(userIdStr),
                new FullName("Hello", "A", "world"),
                new DisplayName("user"));
        UserProfileEntity entity = UserProfileEntity.from(userProfile);
        Mockito.when(mongoTemplate.add(any(UserProfileEntity.class), any(), any(String.class), any()))
                .thenReturn(Mono.just(entity));
        Mockito.when(mongoTemplate.findById(any(), any(String.class), any()))
                .thenReturn(Mono.just(Optional.of(entity)));
        UserProfile savedUserProfile = userProfileRepository.add(userProfile);

        assertThat(savedUserProfile).isNotNull();

        Optional<UserProfile> load = userProfileRepository.load(UserId.fromString(userIdStr));

        assertThat(load).isPresent();
        UserProfile foundUserProfile = load.get();
        assertThat(foundUserProfile.getChanges()).hasSize(1);
    }

}