package com.marketplace.server;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.command.UpdateClassifiedAd;
import com.marketplace.controller.*;
import spark.Spark;

public class SparkServer {
    private final ObjectMapper objectMapper;
    private final ClassifiedAdController classifiedAdController;

    public SparkServer(ObjectMapper objectMapper, ClassifiedAdController classifiedAdController) {
        Spark.port(8000);
        this.objectMapper = objectMapper;
        this.classifiedAdController = classifiedAdController;
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
                var updateClassifiedAdResponse = classifiedAdController.updateClassifiedAd(updateDto);
                return objectMapper.writeValueAsString(updateClassifiedAdResponse);
            } catch (JsonMappingException ex) {
                response.status(502);
            }
            return request.body();
        });
        System.out.println("Spark Server is running on port :" + Spark.port());
    }
}
