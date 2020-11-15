package com.marketplace.server;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.controller.ClassifiedAdController;
import com.marketplace.controller.CreateAdDto;
import com.marketplace.controller.CreateAdResponse;
import com.marketplace.controller.UpdateAdDto;
import spark.Spark;

import javax.inject.Inject;
import javax.inject.Named;
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
            CreateAdDto createAdDto = objectMapper.readValue(body, CreateAdDto.class);
            CreateAdResponse createAdResponse = classifiedAdController.createAd(createAdDto);
            String res = objectMapper.writeValueAsString(createAdResponse);
            response.header("Content-Type", "application/json");
            response.type("application/json");
            response.status(201);
            response.header("Location", String.format("/classified_add/%s", createAdResponse.getId().toString()));
            return res;
        });

        Spark.put("/classified_ad/:classifiedAdId", "application/json", (request, response) -> {
            String classifiedAdId = request.params(":classifiedAdId");
            response.type("application/json");
            try {
                byte[] body = request.bodyAsBytes();
                var updateDto = objectMapper.readValue(body, UpdateAdDto.class);
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
