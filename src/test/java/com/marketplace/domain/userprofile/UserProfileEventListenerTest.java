package com.marketplace.domain.userprofile;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.marketplace.domain.userprofile.entity.UserProfileEntity;
import com.marketplace.domain.userprofile.event.ImmutableUserRegistered;
import com.marketplace.domain.userprofile.event.UserRegistered;
import com.marketplace.domain.userprofile.repository.UserProfileQueryRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserProfileEventListenerTest {

    @Mock
    private UserProfileQueryRepository userProfileQueryRepository;

    private UserProfileEventListener userProfileEventListener;

    @BeforeEach
    void setup() {
        userProfileEventListener = new UserProfileEventListener(userProfileQueryRepository);
    }

    @Test
    void testNewUserCanBeCreated() {
        var userId = UUID.fromString("081b772e-c3c8-4d3a-bbcf-59e526daa4c5");
        var eventId = UUID.fromString("681f96b5-4d81-4b8c-91c8-9bacdcefc7af");
        UserRegistered userRegisteredEvt = ImmutableUserRegistered.builder()
            .id(eventId)
            .userId(userId)
            .aggregateId(userId)
            .firstName("Ellen")
            .lastName("Osei")
            .middleName("Rosebud")
            .userId(userId)
            .displayName("ellenosei")
            .build();

        userProfileEventListener.onUserCreated(userRegisteredEvt);
        UserProfileEntity profileEntity = UserProfileEventListener.createFromEvent(userRegisteredEvt);
        verify(userProfileQueryRepository, times(1)).save(profileEntity);

    }


}