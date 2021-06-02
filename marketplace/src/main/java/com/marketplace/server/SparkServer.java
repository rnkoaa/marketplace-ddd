package com.marketplace.server;

import com.marketplace.server.command.ClassifiedAdCommandSparkRoutes;
import javax.inject.Inject;
import javax.inject.Named;
import spark.Spark;

public class SparkServer {

    public static final String MEDIA_APPLICATION_JSON = "application/json";
    private final ClassifiedAdCommandSparkRoutes classifiedAdCommandSparkRoutes;
    private final ClassifiedAdQuerySparkRoutes classifiedAdQuerySparkRoutes;
    private final EventSparkRoutes eventSparkRoutes;
    private final UserProfileSparkRoutes userProfileSparkRoutes;
    private final AdminService adminService;

    @Inject
    public SparkServer(
        @Named("server.port") int port,
        ClassifiedAdCommandSparkRoutes classifiedAdCommandSparkRoutes,
        EventSparkRoutes eventSparkRoutes,
        ClassifiedAdQuerySparkRoutes classifiedAdQuerySparkRoutes,
        UserProfileSparkRoutes userProfileSparkRoutes, AdminService adminService) {
        this.userProfileSparkRoutes = userProfileSparkRoutes;
        Spark.port(port);
        this.classifiedAdCommandSparkRoutes = classifiedAdCommandSparkRoutes;
        this.classifiedAdQuerySparkRoutes = classifiedAdQuerySparkRoutes;
        this.eventSparkRoutes = eventSparkRoutes;
        this.adminService = adminService;
    }

    public void run() {
        Spark.get("/health", (req, res) -> "ok");

        classifiedAdCommandSparkRoutes.register("/classified_ad");
//
//        Spark.get("/classified_ad/list", classifiedAdQuerySparkRoutes.findAll());

//        Spark.get(
//            "/classified_ad/:classifiedAdId", classifiedAdQuerySparkRoutes.findClassifiedAdById());

        Spark.get("/classified_ad/myads", classifiedAdQuerySparkRoutes.findAll());

        adminService.register("/admin");
        userProfileSparkRoutes.register("/user");
        eventSparkRoutes.register("/event");

        System.out.println("Spark Server is running on port :" + Spark.port());
    }
}
