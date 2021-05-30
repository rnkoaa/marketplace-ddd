package com.marketplace.client.functional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.marketplace.client.model.userprofile.CreateUserProfileRequest;
import com.marketplace.client.model.userprofile.ImmutableCreateUserProfileRequest;
import java.io.IOException;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Call;
import retrofit2.Response;

public class UserProfileFunctionalTest extends AbstractFunctionalTest {

    @Test
    void createUser() throws IOException {
        CreateUserProfileRequest createUserProfileRequest = createUserProfileRequest();

        Call<Void> userProfile = userProfileRestService.createUserProfile(createUserProfileRequest);
        Response<Void> response = userProfile.execute();
        assertThat(response.isSuccessful()).isTrue();

        String location = response.headers().get("Location");
        assertThat(location).isNotEmpty();
    }

    @Test
    void cannotCreateUserWithDuplicateDisplayName() throws IOException {
        CreateUserProfileRequest createUserProfileRequest = createUserProfileRequest();
        Call<Void> userProfile = userProfileRestService.createUserProfile(createUserProfileRequest);

        Response<Void> response = userProfile.execute();
        assertThat(response.isSuccessful()).isTrue();

        String location = response.headers().get("Location");
        assertThat(location).isNotEmpty();

        Call<Void> duplicateCreateResponse = userProfileRestService.createUserProfile(createUserProfileRequest);

        Response<Void> duplicateResponse = duplicateCreateResponse.execute();
        assertThat(duplicateResponse.isSuccessful()).isFalse();
        ResponseBody responseBody = duplicateResponse.errorBody();

        assert responseBody != null;
        byte[] bytes = responseBody.bytes();
        assertThat(new String(bytes)).contains("already exists");
    }

    @AfterEach
    void cleanup() throws IOException {
        userProfileRestService.deleteAllUsers()
            .execute();
    }

    @BeforeEach
    void cleanupBeforeTest() throws IOException {
        userProfileRestService.deleteAllUsers()
            .execute();
    }

    private CreateUserProfileRequest createUserProfileRequest() {
        return ImmutableCreateUserProfileRequest.builder()
            .firstName("Tucci")
            .lastName("Goka")
            .displayName("tuccigoka")
            .build();
    }
}
