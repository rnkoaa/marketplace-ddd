package com.marketplace.client;

import com.marketplace.client.model.userprofile.CreateUserProfileRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserProfileRestService {

    @POST("user")
    Call<Void> createUserProfile(@Body CreateUserProfileRequest createUserProfileRequest);

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
