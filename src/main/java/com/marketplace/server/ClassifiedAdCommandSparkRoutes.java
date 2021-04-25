package com.marketplace.server;

import static com.marketplace.server.SparkServer.MEDIA_APPLICATION_JSON;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.cqrs.command.CommandHandlerResult;
import com.marketplace.domain.classifiedad.command.CreateClassifiedAd;
import com.marketplace.domain.classifiedad.command.ImmutableUpdateClassifiedAd;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd;
import com.marketplace.domain.classifiedad.controller.ClassifiedAdController;
import com.marketplace.domain.classifiedad.controller.CreateAdResponse;
import com.marketplace.domain.classifiedad.controller.UpdateClassifiedAdResponse;
import com.marketplace.eventstore.framework.event.Event;
import io.vavr.control.Try;
import java.util.List;
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
  private final ClassifiedAdController classifiedAdController;

  @Inject
  public ClassifiedAdCommandSparkRoutes(ObjectMapper objectMapper, ClassifiedAdController classifiedAdController) {
    super(objectMapper);
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
        response.header("Location", String.format("/classified_ad/%s", createAdResponse.get()));
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
      var updateDto = read(classifiedAdId, request);
      updateDto = ImmutableUpdateClassifiedAd.copyOf(updateDto).withClassifiedAdId(UUID.fromString(classifiedAdId));
      var commandResult = classifiedAdController.updateClassifiedAd(updateDto);
      return processResponse(response, commandResult);
    };
  }

  public Route updateClassifiedAdTitle() {
    return (req, res) -> {
      String classifiedAdId = getClassifiedIdFromRequest(req);
      var updateClassifiedAd = read(classifiedAdId, req);
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
      var updateClassifiedAd = read(classifiedAdId, req);
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
      var updateClassifiedAd = read(classifiedAdId, req);
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
      var updateClassifiedAd = read(classifiedAdId, req);
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
      var updateClassifiedAd = read(classifiedAdId, req);
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
      var updateClassifiedAd = read(classifiedAdId, req);
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

  private UpdateClassifiedAd read(String classifiedAdId, Request req) {
    byte[] bytes = req.bodyAsBytes();
    UpdateClassifiedAd updateClassifiedAd = Try.of(() -> objectMapper.readValue(bytes, UpdateClassifiedAd.class))
        .onFailure(ex -> {
          System.out.println("error deserializing update request object with error ");
          System.out.println(ex.getMessage());
          LOGGER.info("error deserializing update request object with error {}", ex.getMessage());
        })
        .getOrElseThrow(() -> new IllegalArgumentException("failed to deserialize request body"));
    return ImmutableUpdateClassifiedAd.copyOf(updateClassifiedAd)
        .withClassifiedAdId(UUID.fromString(classifiedAdId));
  }

  public Route createClassifiedAdFromEvents() {
//    byte[] bytes = req.bodyAsBytes();
    return null;
  }
}
