package com.marketplace.client.functional.userprofile;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.marketplace.client.functional.AbstractFunctionalTest;
import com.marketplace.client.userprofile.CreateUserProfileRequest;
import com.marketplace.client.userprofile.GetUserProfileResponse;
import com.marketplace.client.userprofile.ImmutableCreateUserProfileRequest;
import com.marketplace.client.userprofile.ImmutableGetUserProfileResponse;
import com.marketplace.client.userprofile.ImmutableUpdateUserProfileDisplayNameRequest;
import com.marketplace.client.userprofile.ImmutableUpdateUserProfileFullNameRequest;
import com.marketplace.client.userprofile.ImmutableUpdateUserProfilePhotoUrlRequest;
import com.marketplace.client.userprofile.ImmutableUpdateUserProfileRequest;
import com.marketplace.client.userprofile.UpdateUserProfileResponse;
import java.io.IOException;
import java.util.UUID;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import retrofit2.Call;
import retrofit2.Response;

public class UserProfileFunctionalTest extends AbstractFunctionalTest {

    @Test
    void createUserProfile() throws IOException {
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

        UUID userId = createUserProfile(createUserProfileRequest);

        Call<GetUserProfileResponse> userProfileResponseCall = userProfileRestService.getUserProfile(userId);

        Response<GetUserProfileResponse> userProfileExecutionResponse = userProfileResponseCall.execute();
        assertThat(userProfileExecutionResponse.isSuccessful()).isTrue();

        GetUserProfileResponse body = userProfileExecutionResponse.body();
        assertThat(body).isNotNull();
        assertThat(body.getDisplayName()).isEqualTo(createUserProfileRequest.getDisplayName());
        assertThat(body.getUserId()).isEqualTo(userId);
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

    @Nested
    class UserProfileFullName {

        @Test
        void existingUserFullNameCanBeUpdated() throws IOException {
            var user = createUserProfileRequest();
            var userId = createUserProfile(user);

            var updateRequest = ImmutableUpdateUserProfileFullNameRequest.builder()
                .userId(userId)
                .firstName("Shaundai")
                .lastName("Person")
                .build();

            Response<UpdateUserProfileResponse> updateUserProfileResponse = userProfileRestService
                .updateUserProfileFullName(userId, updateRequest)
                .execute();

            assertThat(updateUserProfileResponse.isSuccessful()).isTrue();

            Response<GetUserProfileResponse> userProfileResponse = userProfileRestService.getUserProfile(userId)
                .execute();
            assertThat(userProfileResponse.isSuccessful()).isTrue();

            validateResponseBody(userProfileResponse, ImmutableGetUserProfileResponse.builder()
                .firstName("Shaundai")
                .lastName("Person")
                .displayName(user.getDisplayName())
                .userId(userId)
                .build());
        }

    }

    @Nested
    class UserProfileDisplayName {

        @Test
        void existingUserDisplayCanBeUpdated() throws IOException {
            var user = createUserProfileRequest();
            var userId = createUserProfile(user);

            var updateRequest = ImmutableUpdateUserProfileDisplayNameRequest.builder()
                .userId(userId)
                .displayName("shaundai")
                .build();

            Response<UpdateUserProfileResponse> updateUserProfileResponse = userProfileRestService
                .updateUserProfileDisplayName(userId, updateRequest)
                .execute();

            assertThat(updateUserProfileResponse.isSuccessful()).isTrue();

            Response<GetUserProfileResponse> userProfileResponse = userProfileRestService.getUserProfile(userId)
                .execute();
            assertThat(userProfileResponse.isSuccessful()).isTrue();

            validateResponseBody(userProfileResponse, ImmutableGetUserProfileResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .displayName("shaundai")
                .userId(userId)
                .build());
        }
    }

    @Nested
    class UserProfilePhotoUrl {

        @Test
        void existingUserPhotoUpdated() throws IOException {
            var user = createUserProfileRequest();
            var userId = createUserProfile(user);

            String expectedPhotoUrl = "https://www.sfanonline.org/wp-content/uploads/2019/11/Tucci-7.jpeg";

            var updateRequest = ImmutableUpdateUserProfilePhotoUrlRequest.builder()
                .userId(userId)
                .photoUrl(expectedPhotoUrl)
                .build();

            Response<UpdateUserProfileResponse> updateUserProfileResponse = userProfileRestService
                .updateUserProfilePhoto(userId, updateRequest)
                .execute();

            assertThat(updateUserProfileResponse.isSuccessful()).isTrue();

            Response<GetUserProfileResponse> userProfileResponse = userProfileRestService.getUserProfile(userId)
                .execute();
            assertThat(userProfileResponse.isSuccessful()).isTrue();

            validateResponseBody(userProfileResponse, ImmutableGetUserProfileResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .displayName(user.getDisplayName())
                .photoUrl(expectedPhotoUrl)
                .userId(userId)
                .build());
        }
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

    void validateResponseBody(Response<GetUserProfileResponse> response, GetUserProfileResponse expected) {
        GetUserProfileResponse body = response.body();
        assertThat(body).isNotNull();
        assertThat(expected.getDisplayName()).isEqualTo(body.getDisplayName());

        assertThat(expected.getFirstName()).isEqualTo(body.getFirstName());
        assertThat(expected.getLastName()).isEqualTo(body.getLastName());

        String photoUrl = body.getPhotoUrl().orElse("");
        String expectedPhotoUrl = expected.getPhotoUrl().orElse("");
        assertThat(expectedPhotoUrl).isEqualTo(photoUrl);

        String middleName = body.getMiddleName().orElse("");
        String expectedMiddleName = expected.getMiddleName().orElse("");
        assertThat(expectedMiddleName).isEqualTo(middleName);
    }

    private UUID createUserProfile(CreateUserProfileRequest createUserProfileRequest) throws IOException {
        Call<Void> userProfile = userProfileRestService.createUserProfile(createUserProfileRequest);
        Response<Void> response = userProfile.execute();
        assertThat(response.isSuccessful()).isTrue();

        String location = response.headers().get("Location");
        assertThat(location).isNotNull().isNotEmpty();

        return UUID.fromString(retrieveId(location));
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
