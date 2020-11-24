package com.marketplace.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.domain.classifiedad.CommandHandlerResult;
import com.marketplace.domain.classifiedad.command.CreateClassifiedAd;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd;
import com.marketplace.domain.classifiedad.controller.ClassifiedAdController;
import com.marketplace.domain.classifiedad.controller.CreateAdResponse;
import com.marketplace.domain.classifiedad.query.ClassifiedAdQueryEntity;
import com.marketplace.framework.Strings;
import spark.Request;
import spark.Route;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.marketplace.server.SparkServer.MEDIA_APPLICATION_JSON;

@Singleton
@Named
public class ClassifiedAdCommandSparkRoutes extends ClassifiedAdBaseRoutes {

  public static final String HEADER_CONTENT_TYPE = "Content-Type";
  public static final String NO_CONTENT = "";
  private final ObjectMapper objectMapper;
  private final ClassifiedAdController classifiedAdController;

  @Inject
  public ClassifiedAdCommandSparkRoutes(ObjectMapper objectMapper, ClassifiedAdController classifiedAdController) {
    this.objectMapper = objectMapper;
    this.classifiedAdController = classifiedAdController;
  }

  public Route createClassifiedAdRoute() {
    return (request, response) -> {
      byte[] body = request.bodyAsBytes();
      CreateClassifiedAd createAdDto = objectMapper.readValue(body, CreateClassifiedAd.class);
      CommandHandlerResult<CreateAdResponse> ad = classifiedAdController.createAd(createAdDto);
      if (ad.isSuccessful()) {
        var createAdResponse = ad.result;
        response.header(HEADER_CONTENT_TYPE, MEDIA_APPLICATION_JSON);
        response.type(MEDIA_APPLICATION_JSON);
        response.status(201);
        response.header("Location", String.format("/classified_ad/%s", createAdResponse.getId().toString()));
        return NO_CONTENT;
      } else {
        Map<String, Object> resMessage = Map.of(
            "status", ad.isSuccessful(),
            "message", ad.getMessage()
        );
        String s = objectMapper.writeValueAsString(resMessage);
        response.status(404);
        return s;
      }
    };
  }

  public Route updateClassifiedAd() {
    return (request, response) -> {
      String classifiedAdId = getClassifiedIdFromRequest(request);
      response.type("application/json");
      try {
        byte[] body = request.bodyAsBytes();
        var updateDto = objectMapper.readValue(body, UpdateClassifiedAd.class);
        updateDto.setId(UUID.fromString(classifiedAdId));
        var commandResult = classifiedAdController.updateClassifiedAd(updateDto);
        if (commandResult.isSuccessful()) {
          response.status(204);
          return NO_CONTENT;
        } else {
          Map<String, Object> resMessage = Map.of(
              "status", commandResult.isSuccessful(),
              "message", commandResult.getMessage()
          );
          String s = objectMapper.writeValueAsString(resMessage);
          response.status(404);
          return s;
        }
      } catch (JsonMappingException ex) {
        response.status(502);
      }
      return null;
    };
  }

  public Route updateClassifiedAdTitle() {
    return (req, res) -> {
      String classifiedAdId = getClassifiedIdFromRequest(req);
      var updateClassifiedAd = read(req);
      if (Strings.isNullOrEmpty(updateClassifiedAd.getTitle())) {
        Map<String, Object> resMessage = Map.of(
            "status", false,
            "message", "title is required to change ownership of ad"
        );
        res.status(404);
        return objectMapper.writeValueAsString(resMessage);
      }

      var commandResult = classifiedAdController
          .updateClassifiedTitle(UUID.fromString(classifiedAdId), updateClassifiedAd.getTitle());
      if (commandResult.isSuccessful()) {
        res.status(204);
        return NO_CONTENT;
      } else {
        Map<String, Object> resMessage = Map.of(
            "status", commandResult.isSuccessful(),
            "message", commandResult.getMessage()
        );
        String s = objectMapper.writeValueAsString(resMessage);
        res.status(404);
        return s;
      }
    };
  }

  public Route updateClassifiedAdOwner() {
    return (req, res) -> {
      String classifiedAdId = getClassifiedIdFromRequest(req);
      var updateClassifiedAd = read(req);
      if (updateClassifiedAd.getOwnerId() == null) {
        Map<String, Object> resMessage = Map.of(
            "status", false,
            "message", "ownerId is required to change ownership of ad"
        );
        res.status(404);
        return objectMapper.writeValueAsString(resMessage);
      }

      var commandResult = classifiedAdController
          .updateClassifiedAdOwner(UUID.fromString(classifiedAdId), updateClassifiedAd.getOwnerId());
      if (commandResult.isSuccessful()) {
        res.status(204);
        return NO_CONTENT;
      } else {
        Map<String, Object> resMessage = Map.of(
            "status", commandResult.isSuccessful(),
            "message", commandResult.getMessage()
        );
        String s = objectMapper.writeValueAsString(resMessage);
        res.status(404);
        return s;
      }
    };
  }

  public Route updateClassifiedAdPrice() {
    return (req, res) -> {
      String classifiedAdId = getClassifiedIdFromRequest(req);
      var updateClassifiedAd = read(req);
      if (updateClassifiedAd.getPrice() == null) {
        Map<String, Object> resMessage = Map.of(
            "status", false,
            "message", "price is required to change price of ad"
        );
        res.status(404);
        return objectMapper.writeValueAsString(resMessage);
      }

      var commandResult = classifiedAdController
          .updateClassifiedAdPrice(UUID.fromString(classifiedAdId),
              updateClassifiedAd.getPrice().getAmount(),
              updateClassifiedAd.getPrice().getCurrencyCode());
      if (commandResult.isSuccessful()) {
        res.status(204);
        return NO_CONTENT;
      } else {
        Map<String, Object> resMessage = Map.of(
            "status", commandResult.isSuccessful(),
            "message", commandResult.getMessage()
        );
        String s = objectMapper.writeValueAsString(resMessage);
        res.status(404);
        return s;
      }
    };
  }

  public Route updateClassifiedAdText() {
    return (req, res) -> {
      String classifiedAdId = getClassifiedIdFromRequest(req);
      var updateClassifiedAd = read(req);
      if (Strings.isNullOrEmpty(updateClassifiedAd.getText())) {
        Map<String, Object> resMessage = Map.of(
            "status", false,
            "message", "text is required to change text of ad"
        );
        res.status(404);
        return objectMapper.writeValueAsString(resMessage);
      }

      var commandResult = classifiedAdController
          .updateClassifiedText(UUID.fromString(classifiedAdId), updateClassifiedAd.getText());
      if (commandResult.isSuccessful()) {
        res.status(204);
        return NO_CONTENT;
      } else {
        Map<String, Object> resMessage = Map.of(
            "status", commandResult.isSuccessful(),
            "message", commandResult.getMessage()
        );
        String s = objectMapper.writeValueAsString(resMessage);
        res.status(404);
        return s;
      }
    };
  }

  public Route approveClassifiedAd() {
    return (req, res) -> {
      String classifiedAdId = getClassifiedIdFromRequest(req);
      var updateClassifiedAd = read(req);
      if (updateClassifiedAd.getApprovedBy() == null) {
        Map<String, Object> resMessage = Map.of(
            "status", false,
            "message", "approver is required "
        );
        res.status(404);
        return objectMapper.writeValueAsString(resMessage);
      }

      var commandResult = classifiedAdController
          .approveClassifiedAd(UUID.fromString(classifiedAdId), updateClassifiedAd.getApprovedBy());
      if (commandResult.isSuccessful()) {
        res.status(204);
        return NO_CONTENT;
      } else {
        Map<String, Object> resMessage = Map.of(
            "status", commandResult.isSuccessful(),
            "message", commandResult.getMessage()
        );
        String s = objectMapper.writeValueAsString(resMessage);
        res.status(404);
        return s;
      }
    };
  }

  public Route publishClassifiedAd() {
    return (req, res) -> {
      String classifiedAdId = getClassifiedIdFromRequest(req);
      var commandResult = classifiedAdController
          .publishClassifiedAd(UUID.fromString(classifiedAdId));
      if (commandResult.isSuccessful()) {
        res.status(204);
        return NO_CONTENT;
      } else {
        Map<String, Object> resMessage = Map.of(
            "status", commandResult.isSuccessful(),
            "message", commandResult.getMessage()
        );
        String s = objectMapper.writeValueAsString(resMessage);
        res.status(404);
        return s;
      }
    };
  }

  public Route addPictureToClassifiedAd() {
    return (req, res) -> {
      String classifiedAdId = getClassifiedIdFromRequest(req);
      var updateClassifiedAd = read(req);
      if (updateClassifiedAd.getPictures() == null || updateClassifiedAd.getPictures().size() == 0) {
        Map<String, Object> resMessage = Map.of(
            "status", false,
            "message", "at least add a picture to be added to classifiedAd"
        );
        String s = objectMapper.writeValueAsString(resMessage);
        res.status(404);
        return s;
      }

      var commandResult = classifiedAdController.addPictures(UUID.fromString(classifiedAdId), updateClassifiedAd.getPictures());
      if (commandResult.isSuccessful()) {
        res.status(204);
        return NO_CONTENT;
      } else {
        Map<String, Object> resMessage = Map.of(
            "status", commandResult.isSuccessful(),
            "message", commandResult.getMessage()
        );
        String s = objectMapper.writeValueAsString(resMessage);
        res.status(404);
        return s;
      }
    };
  }

  private UpdateClassifiedAd read(Request req) throws IOException {
    byte[] bytes = req.bodyAsBytes();
    return objectMapper.readValue(bytes, UpdateClassifiedAd.class);
  }

}
