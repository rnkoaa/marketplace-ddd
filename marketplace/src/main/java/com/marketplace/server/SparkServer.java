package com.marketplace.server;

import com.marketplace.server.command.UserProfileCommandRoutes;
import spark.Spark;

import javax.inject.Inject;
import javax.inject.Named;

public class SparkServer {

    public static final String MEDIA_APPLICATION_JSON = "application/json";
    private final ClassifiedAdCommandSparkRoutes classifiedAdCommandSparkRoutes;
    private final ClassifiedAdQuerySparkRoutes classifiedAdQuerySparkRoutes;
    private final UserProfileCommandRoutes userProfileCommandSparkRoutes;
    private final EventSparkRoutes eventSparkRoutes;
    private final AdminService adminService;

    @Inject
    public SparkServer(
        @Named("server.port") int port,
        ClassifiedAdCommandSparkRoutes classifiedAdCommandSparkRoutes,
        UserProfileCommandRoutes userProfileCommandSparkRoutes,
        EventSparkRoutes eventSparkRoutes,
        ClassifiedAdQuerySparkRoutes classifiedAdQuerySparkRoutes,
        AdminService adminService) {
        Spark.port(port);
        this.classifiedAdCommandSparkRoutes = classifiedAdCommandSparkRoutes;
        this.classifiedAdQuerySparkRoutes = classifiedAdQuerySparkRoutes;
        this.userProfileCommandSparkRoutes = userProfileCommandSparkRoutes;
        this.eventSparkRoutes = eventSparkRoutes;
        this.adminService = adminService;
    }

    public void run() {
        Spark.get("/health", (req, res) -> "ok");

        Spark.post(
            "/classified_ad",
            MEDIA_APPLICATION_JSON,
            classifiedAdCommandSparkRoutes.createClassifiedAdRoute());

        Spark.put(
            "/classified_ad/:classifiedAdId",
            MEDIA_APPLICATION_JSON,
            classifiedAdCommandSparkRoutes.updateClassifiedAd());

        Spark.put(
            "/classified_ad/:classifiedAdId/title",
            MEDIA_APPLICATION_JSON,
            classifiedAdCommandSparkRoutes.updateClassifiedAdTitle());

        Spark.put(
            "/classified_ad/:classifiedAdId/owner",
            MEDIA_APPLICATION_JSON,
            classifiedAdCommandSparkRoutes.updateClassifiedAdOwner());

        Spark.put(
            "/classified_ad/:classifiedAdId/text",
            MEDIA_APPLICATION_JSON,
            classifiedAdCommandSparkRoutes.updateClassifiedAdText());

        Spark.put(
            "/classified_ad/:classifiedAdId/price",
            MEDIA_APPLICATION_JSON,
            classifiedAdCommandSparkRoutes.updateClassifiedAdPrice());

        Spark.put(
            "/classified_ad/:classifiedAdId/approve",
            MEDIA_APPLICATION_JSON,
            classifiedAdCommandSparkRoutes.approveClassifiedAd());

        Spark.put(
            "/classified_ad/:classifiedAdId/publish",
            MEDIA_APPLICATION_JSON,
            classifiedAdCommandSparkRoutes.publishClassifiedAd());

        Spark.put(
            "/classified_ad/:classifiedAdId/pictures",
            MEDIA_APPLICATION_JSON,
            classifiedAdCommandSparkRoutes.addPictureToClassifiedAd());

        Spark.get("/classified_ad/list", classifiedAdQuerySparkRoutes.findAll());

        Spark.get(
            "/classified_ad/:classifiedAdId", classifiedAdQuerySparkRoutes.findClassifiedAdById());

        Spark.get("/classified_ad/myads", classifiedAdQuerySparkRoutes.findAll());

        /* user profile ***/
        /* **/
        Spark.post(
            "/user",
            MEDIA_APPLICATION_JSON,
            userProfileCommandSparkRoutes.createUserProfile());
        Spark.put(
            "/user/:userId",
            MEDIA_APPLICATION_JSON,
            userProfileCommandSparkRoutes.updateUserProfile());
        Spark.get(
            "/user/:userId",
            MEDIA_APPLICATION_JSON,
            userProfileCommandSparkRoutes.getUserProfile());
        Spark.put(
            "/user/:userId/name",
            MEDIA_APPLICATION_JSON,
            userProfileCommandSparkRoutes.updateUserFullName());
        Spark.put(
            "/user/:userId/photo",
            MEDIA_APPLICATION_JSON,
            userProfileCommandSparkRoutes.updateUserProfilePhoto());
        Spark.put(
            "/user/:userId/display_name",
            MEDIA_APPLICATION_JSON,
            userProfileCommandSparkRoutes.updateUserDisplayName());

        adminService.register("/admin");
        eventSparkRoutes.register("/event");

        System.out.println("Spark Server is running on port :" + Spark.port());
    }
}
