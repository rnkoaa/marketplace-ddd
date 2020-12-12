package com.marketplace.server;

import static com.marketplace.server.SparkServer.MEDIA_APPLICATION_JSON;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.domain.classifiedad.CommandHandlerResult;
import com.marketplace.domain.classifiedad.command.CreateClassifiedAd;
import com.marketplace.domain.classifiedad.command.ImmutableUpdateClassifiedAd;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd;
import com.marketplace.domain.classifiedad.controller.ClassifiedAdController;
import com.marketplace.domain.classifiedad.controller.CreateAdResponse;
import com.marketplace.domain.classifiedad.controller.UpdateClassifiedAdResponse;
import io.vavr.control.Try;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

@Singleton
@Named
public class ClassifiedAdCommandSparkRoutes extends ClassifiedAdBaseRoutes {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClassifiedAdCommandSparkRoutes.class);
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
      if (ad.isSuccessful() && ad.getResult().isPresent()) {
        var createAdResponse = ad.getResult();
        response.header(HEADER_CONTENT_TYPE, MEDIA_APPLICATION_JSON);
        response.type(MEDIA_APPLICATION_JSON);
        response.status(201);
        response.header("Location", String.format("/classified_ad/%s", createAdResponse.get().toString()));
        return NO_CONTENT;
      } else {
        Map<String, Object> resMessage = Map.of(
            "status", ad.isSuccessful(),
            "message", ad.getMessage()
        );
        response.status(404);
        return serializeResponse(resMessage);
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
        updateDto = ImmutableUpdateClassifiedAd.copyOf(updateDto).withClassifiedAdId(UUID.fromString(classifiedAdId));
        var commandResult = classifiedAdController.updateClassifiedAd(updateDto);
        return processResponse(response, commandResult);
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
      return updateClassifiedAd.getTitle().map(title -> {
        var commandResult = classifiedAdController
            .updateClassifiedTitle(UUID.fromString(classifiedAdId), title);
        return processUpdateResponse(res, commandResult);
      }).orElseGet(() -> {
        Map<String, Object> resMessage = Map.of(
            "status", false,
            "message", "title is required to change ownership of ad"
        );
        res.status(404);
        return serializeResponse(resMessage);
      });
    };
  }

  public Route updateClassifiedAdOwner() {
    return (req, res) -> {
      String classifiedAdId = getClassifiedIdFromRequest(req);
      var updateClassifiedAd = read(req);
      return updateClassifiedAd.getOwnerId().map(ownerId -> {
        var commandResult = classifiedAdController
            .updateClassifiedAdOwner(UUID.fromString(classifiedAdId), ownerId);
        return processUpdateResponse(res, commandResult);
      }).orElseGet(() -> {
        Map<String, Object> resMessage = Map.of(
            "status", false,
            "message", "ownerId is required to change ownership of ad"
        );
        res.status(404);
        return serializeResponse(resMessage);
      });
    };
  }

  public Route updateClassifiedAdPrice() {
    return (req, res) -> {
      String classifiedAdId = getClassifiedIdFromRequest(req);
      var updateClassifiedAd = read(req);
      return updateClassifiedAd.getPrice()
          .map(priceDto -> {
            var commandResult = classifiedAdController
                .updateClassifiedAdPrice(UUID.fromString(classifiedAdId),
                    priceDto.getAmount(),
                    priceDto.getCurrencyCode());
            return processUpdateResponse(res, commandResult);
          }).orElseGet(() -> {
            Map<String, Object> resMessage = Map.of(
                "status", false,
                "message", "price is required to change price of ad"
            );
            res.status(404);
            return serializeResponse(resMessage);
          });
    };
  }

  public Route updateClassifiedAdText() {
    return (req, res) -> {
      String classifiedAdId = getClassifiedIdFromRequest(req);
      var updateClassifiedAd = read(req);
      return updateClassifiedAd.getText()
          .map(text -> {
            var commandResult = classifiedAdController
                .updateClassifiedText(UUID.fromString(classifiedAdId), text);
            return processUpdateResponse(res, commandResult);
          })
          .orElseGet(() -> {
            Map<String, Object> resMessage = Map.of(
                "status", false,
                "message", "text is required to change text of ad"
            );
            res.status(404);
            return serializeResponse(resMessage);
          });
    };
  }

  public Route approveClassifiedAd() {
    return (req, res) -> {
      String classifiedAdId = getClassifiedIdFromRequest(req);
      var updateClassifiedAd = read(req);
      return updateClassifiedAd.getApprovedBy()
          .map(approvedBy -> {
            var commandResult = classifiedAdController
                .approveClassifiedAd(UUID.fromString(classifiedAdId), approvedBy);
            return processUpdateResponse(res, commandResult);
          }).orElseGet(() -> {

            Map<String, Object> resMessage = Map.of(
                "status", false,
                "message", "approver is required "
            );
            res.status(404);
            return Try.of(() -> objectMapper.writeValueAsString(resMessage))
                .get();
          });
    };
  }

  private String processUpdateResponse(Response res, CommandHandlerResult<UpdateClassifiedAdResponse> commandResult) {
    if (commandResult.isSuccessful()) {
      res.status(204);
      return NO_CONTENT;
    } else {
      Map<String, Object> resMessage = Map.of(
          "status", commandResult.isSuccessful(),
          "message", commandResult.getMessage()
      );
      res.status(404);
      return serializeResponse(resMessage);
    }
  }

  public Route publishClassifiedAd() {
    return (req, res) -> {
      String classifiedAdId = getClassifiedIdFromRequest(req);
      var commandResult = classifiedAdController
          .publishClassifiedAd(UUID.fromString(classifiedAdId));
      return processResponse(res, commandResult);
    };
  }

  private Object processResponse(Response res, CommandHandlerResult<UpdateClassifiedAdResponse> commandResult) {
    if (commandResult.isSuccessful()) {
      res.status(204);
      return NO_CONTENT;
    } else {
      Map<String, Object> resMessage = Map.of(
          "status", commandResult.isSuccessful(),
          "message", commandResult.getMessage()
      );
      res.status(404);
      return serializeResponse(resMessage);
    }
  }

  public Route addPictureToClassifiedAd() {
    return (req, res) -> {
      String classifiedAdId = getClassifiedIdFromRequest(req);
      var updateClassifiedAd = read(req);
      return updateClassifiedAd.getPictures()
          .map(pictureDtos -> {
            var commandResult = classifiedAdController.addPictures(UUID.fromString(classifiedAdId), pictureDtos);
            return processResponse(res, commandResult);
          }).orElseGet(() -> {
            Map<String, Object> resMessage = Map.of(
                "status", false,
                "message", "at least add a picture to be added to classifiedAd"
            );
            res.status(404);
            return serializeResponse(resMessage);
          });
    };
  }

  public String serializeResponse(Object object) {
    return Try.of(() -> objectMapper.writeValueAsString(object))
        .onFailure(ex -> LOGGER.info("error while serialing object with message {}", ex.getMessage()))
        .getOrElse("");
  }

  private UpdateClassifiedAd read(Request req) throws IOException {
    byte[] bytes = req.bodyAsBytes();
    return objectMapper.readValue(bytes, UpdateClassifiedAd.class);
  }

}
