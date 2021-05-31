package com.marketplace.client.userprofile;

import java.util.UUID;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserProfileRestService {

    /**
     * Retrieves an existing user
     *
     * @param userId id of user to find
     * @return
     */
    @GET("user/{userId}")
    Call<GetUserProfileResponse> getUserProfile(@Path("userId") UUID userId);

    /**
     * Creates a new user
     *
     * @param createUserProfileRequest body of request
     * @return
     */
    @POST("user")
    Call<Void> createUserProfile(@Body CreateUserProfileRequest createUserProfileRequest);

    @PUT("user/{userId}")
    Call<UpdateUserProfileResponse> updateUserProfile(
        @Path("userId") UUID userId,
        @Body UpdateUserProfileRequest updateUserProfileRequest
    );

    @PUT("user/{userId}/photo")
    Call<UpdateUserProfileResponse> updateUserProfilePhoto(
        @Path("userId") UUID userId,
        @Body UpdateUserProfilePhotoUrlRequest updateUserProfilePhotoUrlRequest
    );

    @PUT("user/{userId}/name")
    Call<UpdateUserProfileResponse> updateUserProfileFullName(
        @Path("userId") UUID userId,
        @Body UpdateUserProfileFullNameRequest updateUserProfileFullNameRequest
    );

    @PUT("user/{userId}/display_name")
    Call<UpdateUserProfileResponse> updateUserProfileDisplayName(
        @Path("userId") UUID userId,
        @Body UpdateUserProfileDisplayNameRequest updateUserProfileDisplayNameRequest
    );

    /**
     * Deletes a user and all its events
     *
     * @param userId the user id to be deleted
     * @return
     */
    @DELETE("user/{userId}")
    Call<Void> cleanupUser(@Path("userId") String userId);

    /**
     * deletes all users, this is only enabled for testing.
     *
     * @return
     */
    @DELETE("admin/user")
    Call<Void> deleteAllUsers();

}
