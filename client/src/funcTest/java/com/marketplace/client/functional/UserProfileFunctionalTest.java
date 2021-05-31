package com.marketplace.client.functional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.marketplace.client.model.userprofile.CreateUserProfileRequest;
import com.marketplace.client.model.userprofile.GetUserProfileResponse;
import com.marketplace.client.model.userprofile.ImmutableCreateUserProfileRequest;
import com.marketplace.client.model.userprofile.ImmutableUpdateUserProfileRequest;
import com.marketplace.client.model.userprofile.UpdateUserProfileResponse;
import java.io.IOException;
import java.util.UUID;
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
    void existingUserCanBeRequested() throws IOException {
        CreateUserProfileRequest createUserProfileRequest = createUserProfileRequest();

        Call<Void> userProfile = userProfileRestService.createUserProfile(createUserProfileRequest);
        Response<Void> response = userProfile.execute();
        assertThat(response.isSuccessful()).isTrue();

        String location = response.headers().get("Location");
        assertThat(location).isNotNull().isNotEmpty();

        String userId = retrieveId(location);

        Call<GetUserProfileResponse> userProfileResponseCall = userProfileRestService
            .getUserProfile(UUID.fromString(userId));

        Response<GetUserProfileResponse> userProfileExecutionResponse = userProfileResponseCall.execute();
        assertThat(userProfileExecutionResponse.isSuccessful()).isTrue();

        GetUserProfileResponse body = userProfileExecutionResponse.body();
        assertThat(body).isNotNull();
        assertThat(body.getDisplayName()).isEqualTo(createUserProfileRequest.getDisplayName());
        assertThat(body.getUserId()).isEqualTo(UUID.fromString(userId));
        assertThat(body.getFirstName()).isEqualTo(createUserProfileRequest.getFirstName());
        assertThat(body.getLastName()).isEqualTo(createUserProfileRequest.getLastName());
    }

    @Test
    void existingUserCanUpdatedBeRequested() throws IOException {
        CreateUserProfileRequest createUserProfileRequest = createUserProfileRequest();

        Call<Void> userProfile = userProfileRestService.createUserProfile(createUserProfileRequest);
        Response<Void> response = userProfile.execute();
        assertThat(response.isSuccessful()).isTrue();

        String location = response.headers().get("Location");
        assertThat(location).isNotNull().isNotEmpty();

        var userId = UUID.fromString(retrieveId(location));

        var updateUserProfileRequest = ImmutableUpdateUserProfileRequest.builder()
            .userId(userId)
            .firstName("Shaundai")
            .lastName("Person")
            .displayName("shaundai")
            .build();

        var updateUserCall = userProfileRestService.updateUserProfile(userId, updateUserProfileRequest);
        Response<UpdateUserProfileResponse> execute = updateUserCall.execute();
        assertThat(execute.isSuccessful()).isTrue();

        Call<GetUserProfileResponse> userProfileResponseCall = userProfileRestService
            .getUserProfile(userId);

        Response<GetUserProfileResponse> userProfileExecutionResponse = userProfileResponseCall.execute();
        assertThat(userProfileExecutionResponse.isSuccessful()).isTrue();

        GetUserProfileResponse body = userProfileExecutionResponse.body();
        assertThat(body).isNotNull();
        assertThat(body.getDisplayName()).isEqualTo("shaundai");
        assertThat(body.getUserId()).isEqualTo(userId);
        assertThat(body.getFirstName()).isEqualTo("Shaundai");
        assertThat(body.getLastName()).isEqualTo("Person");
    }

    @Test
    void cannotUpdateANonExistentUserProfile() throws IOException {
        var testUserId = UUID.randomUUID();
        var updateUserProfile = ImmutableUpdateUserProfileRequest.builder()
            .userId(testUserId)
            .displayName("tuccigoka")
            .build();
        var userProfileResponseCall = userProfileRestService.updateUserProfile(testUserId, updateUserProfile);

        Response<UpdateUserProfileResponse> response = userProfileResponseCall.execute();
        assertThat(response.isSuccessful()).isFalse();

        ResponseBody responseBody = response.errorBody();
        assertThat(responseBody).isNotNull();

        byte[] bytes = responseBody.bytes();
        assertThat(bytes).isNotNull();

        assertThat(new String(bytes)).isNotEmpty();
        assertThat(new String(bytes)).contains("user with profile '" + testUserId + "' not found");
    }

    @Test
    void missingUserReturnsEmpty() throws IOException {
        var testUserId = UUID.randomUUID();
        Call<GetUserProfileResponse> userProfileResponseCall = userProfileRestService
            .getUserProfile(testUserId);

        Response<GetUserProfileResponse> userProfileExecutionResponse = userProfileResponseCall.execute();
        assertThat(userProfileExecutionResponse.isSuccessful()).isFalse();

        ResponseBody responseBody = userProfileExecutionResponse.errorBody();
        assertThat(responseBody).isNotNull();
        byte[] bytes = responseBody.bytes();
        System.out.println(new String(bytes));
        assertThat(new String(bytes)).contains("user with profile '" + testUserId + "' not found");
    }

    @Test
    void existingUserCanBeUpdated() throws IOException {
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

    private String retrieveId(String location) {
        String[] split = location.split("/");
        if (split.length > 0) {
            return split[split.length - 1];
        }
        return "";
    }

    private CreateUserProfileRequest createUserProfileRequest() {
        return ImmutableCreateUserProfileRequest.builder()
            .firstName("Tucci")
            .lastName("Goka")
            .displayName("tuccigoka")
            .build();
    }
}
