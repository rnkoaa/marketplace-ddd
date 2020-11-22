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
    private final ObjectMapper objectMapper;
    private final ClassifiedAdController classifiedAdController;

    @Inject
    public SparkServer(@Named("server.port") int port, ObjectMapper objectMapper, ClassifiedAdController controller) {
        Spark.port(port);
        this.objectMapper = objectMapper;
        this.classifiedAdController = controller;
    }

    public void run() {
        Spark.get("/health", (req, res) -> "ok");

        Spark.post("/classified_ad", "application/json", (request, response) -> {
            byte[] body = request.bodyAsBytes();
            CreateClassifiedAd createAdDto = objectMapper.readValue(body, CreateClassifiedAd.class);
            CreateAdResponse createAdResponse = classifiedAdController.createAd(createAdDto);
            String res = objectMapper.writeValueAsString(createAdResponse);
            response.header("Content-Type", "application/json");
            response.type("application/json");
            response.status(201);
            response.header("Location", String.format("/classified_add/%s", createAdResponse.getId().toString()));
            return res;
        });

//        Spark.get("/classified_ad/:classifiedAdId", "application/json", (request, response) -> {
//            String classifiedAdId = request.params(":classifiedAdId");
//            response.type("application/json");
//            Optional<ClassifiedAd> mayBe = classifiedAdController.findClassifiedAdById(ClassifiedAdId.fromString(classifiedAdId));
//                mayBe.ifPresentOrElse(classifiedAd -> {
//                    try {
//                    return objectMapper.writeValueAsString(classifiedAd);
//                    } catch (JsonProcessingException e) {
//                        e.printStackTrace();
//                    }
//                }, () -> {
//                    response.status(502);
//                });
//        });
        Spark.get("/classified_ad/:classifiedAdId", (request, response) -> {
            String classifiedAdId = request.params(":classifiedAdId");
            Optional<ClassifiedAd> mayBe = classifiedAdController
                    .findClassifiedAdById(ClassifiedAdId.fromString(classifiedAdId));
            return mayBe.map(classifiedAd -> {
                String result = null;
                try {
                    result = objectMapper.writeValueAsString(classifiedAd);
                    response.status(200);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return result;
            }).orElseGet(() -> {
                response.status(404);
                return null;
            });
        });
        Spark.put("/classified_ad/:classifiedAdId", "application/json", (request, response) -> {
            String classifiedAdId = request.params(":classifiedAdId");
            response.type("application/json");
            try {
                byte[] body = request.bodyAsBytes();
                var updateDto = objectMapper.readValue(body, UpdateClassifiedAd.class);
                updateDto.setId(UUID.fromString(classifiedAdId));
                var updateClassifiedAdResponse = classifiedAdController.updateClassifiedAd(updateDto);
                updateClassifiedAdResponse.setId(UUID.fromString(classifiedAdId));
                return objectMapper.writeValueAsString(updateClassifiedAdResponse);
            } catch (JsonMappingException ex) {
                response.status(502);
            }
            return request.body();
        });

        System.out.println("Spark Server is running on port :" + Spark.port());
    }
}
