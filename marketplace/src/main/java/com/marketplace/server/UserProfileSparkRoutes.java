package com.marketplace.server;

import static com.marketplace.server.SparkServer.MEDIA_APPLICATION_JSON;

import com.marketplace.server.command.UserProfileCommandRoutes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import spark.Spark;

@Named
@Singleton
public class UserProfileSparkRoutes {

    private final UserProfileCommandRoutes userProfileCommandSparkRoutes;

    @Inject
    public UserProfileSparkRoutes(UserProfileCommandRoutes userProfileCommandSparkRoutes) {
        this.userProfileCommandSparkRoutes = userProfileCommandSparkRoutes;
    }

    public void register(String baseEndpoint) {

        String userIdBasedEndpoint = String.format("%s/:userId", baseEndpoint);
        /* user profile ***/
        /* **/
        Spark.post(
            baseEndpoint,
            MEDIA_APPLICATION_JSON,
            userProfileCommandSparkRoutes.createUserProfile());
        Spark.put(
            userIdBasedEndpoint,
            MEDIA_APPLICATION_JSON,
            userProfileCommandSparkRoutes.updateUserProfile());
        Spark.get(
            userIdBasedEndpoint,
            MEDIA_APPLICATION_JSON,
            userProfileCommandSparkRoutes.getUserProfile());
        Spark.put(
            String.format("%s/name", userIdBasedEndpoint),
            MEDIA_APPLICATION_JSON,
            userProfileCommandSparkRoutes.updateUserFullName());
        Spark.put(
            String.format("%s/photo", userIdBasedEndpoint),
            MEDIA_APPLICATION_JSON,
            userProfileCommandSparkRoutes.updateUserProfilePhoto());
        Spark.put(
            String.format("%s/display_name", userIdBasedEndpoint),
            MEDIA_APPLICATION_JSON,
            userProfileCommandSparkRoutes.updateUserDisplayName());
    }

}
