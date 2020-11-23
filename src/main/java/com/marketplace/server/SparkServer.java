package com.marketplace.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.domain.classifiedad.command.CreateClassifiedAd;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd;
import com.marketplace.domain.classifiedad.controller.ClassifiedAdController;
import com.marketplace.domain.classifiedad.controller.CreateAdResponse;
import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import spark.Spark;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;
import java.util.UUID;

public class SparkServer {
    public static final String MEDIA_APPLICATION_JSON = "application/json";
    private final ClassifiedAdSparkRoutes classifiedAdSparkRoutes;

    @Inject
    public SparkServer(@Named("server.port") int port, ClassifiedAdSparkRoutes classifiedAdSparkRoutes) {
        Spark.port(port);
        this.classifiedAdSparkRoutes = classifiedAdSparkRoutes;
    }

    public void run() {
        Spark.get("/health", (req, res) -> "ok");

        Spark.post("/classified_ad", MEDIA_APPLICATION_JSON, classifiedAdSparkRoutes.createClassifiedAdRoute());

        Spark.get("/classified_ad/:classifiedAdId", classifiedAdSparkRoutes.findClassifiedAdById());

        Spark.put("/classified_ad/:classifiedAdId",
                MEDIA_APPLICATION_JSON,
                classifiedAdSparkRoutes.updateClassifiedAd());

        Spark.put("/classified_ad/:classifiedAdId/title",
                MEDIA_APPLICATION_JSON,
                classifiedAdSparkRoutes.updateClassifiedAdTitle());

        Spark.put("/classified_ad/:classifiedAdId/owner",
                MEDIA_APPLICATION_JSON,
                classifiedAdSparkRoutes.updateClassifiedAdOwner());

        Spark.put("/classified_ad/:classifiedAdId/text",
                MEDIA_APPLICATION_JSON,
                classifiedAdSparkRoutes.updateClassifiedAdText());

        Spark.put("/classified_ad/:classifiedAdId/price",
                MEDIA_APPLICATION_JSON,
                classifiedAdSparkRoutes.updateClassifiedAdPrice());

        Spark.put("/classified_ad/:classifiedAdId/approve",
                MEDIA_APPLICATION_JSON,
                classifiedAdSparkRoutes.approveClassifiedAd());

        Spark.put("/classified_ad/:classifiedAdId/publish",
                MEDIA_APPLICATION_JSON,
                classifiedAdSparkRoutes.publishClassifiedAd());

        Spark.put("/classified_ad/:classifiedAdId/pictures",
                MEDIA_APPLICATION_JSON,
                classifiedAdSparkRoutes.addPictureToClassifiedAd());

        System.out.println("Spark Server is running on port :" + Spark.port());
    }
}
