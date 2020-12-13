package com.marketplace.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.ClassifiedAdState;
import com.marketplace.domain.classifiedad.CommandHandlerResult;
import com.marketplace.domain.classifiedad.command.CreateClassifiedAd;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd;
import com.marketplace.domain.classifiedad.controller.ClassifiedAdController;
import com.marketplace.domain.classifiedad.controller.CreateAdResponse;
import com.marketplace.domain.classifiedad.query.ClassifiedAdQueryController;
import com.marketplace.domain.classifiedad.query.ClassifiedAdQueryEntity;
import com.marketplace.domain.classifiedad.service.ClassifiedAdService;
import com.marketplace.framework.Strings;
import spark.QueryParamsMap;
import spark.Request;
import spark.Route;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.marketplace.server.SparkServer.MEDIA_APPLICATION_JSON;

@Singleton
@Named
public class ClassifiedAdQuerySparkRoutes extends ClassifiedAdBaseRoutes {

  public static final String HEADER_CONTENT_TYPE = "Content-Type";
  public static final String NO_CONTENT = "";
  //  private final ObjectMapper objectMapper;
  private final ClassifiedAdQueryController classifiedAdQueryController;
  private final ClassifiedAdService classifiedAdService;

  @Inject
  public ClassifiedAdQuerySparkRoutes(
      ObjectMapper objectMapper, ClassifiedAdQueryController classifiedAdQueryController,
      ClassifiedAdService classifiedAdService) {
    super(objectMapper);
    this.classifiedAdQueryController = classifiedAdQueryController;
    this.classifiedAdService = classifiedAdService;
  }

  public Route findClassifiedAdById() {
    return (request, response) -> {
      response.type("application/json");
      String classifiedAdId = getClassifiedIdFromRequest(request);
      Optional<ClassifiedAdQueryEntity> mayBe =
          classifiedAdQueryController.findEntityById(UUID.fromString(classifiedAdId));
      return mayBe
          .map(item -> {
            response.status(200);
            return serializeResponse(item);
          })
          .orElseGet(
              () -> {
                response.status(404);
                return null;
              });
    };
  }

  public Route findAll() {
    return (request, response) -> {
      response.type("application/json");
      String owner = request.queryParams("owner");
      String status = request.queryParams("state");
      if (Strings.isNullOrEmpty(owner) && Strings.isNullOrEmpty(status)) {
        List<ClassifiedAdQueryEntity> items = classifiedAdQueryController.findAll();

        response.status(200);
        return serializeResponse(items);
      }

      var ownerUuid = (!Strings.isNullOrEmpty(owner)) ? UUID.fromString(owner) : null;
      var classifiedAdState =
          (!Strings.isNullOrEmpty(status))
              ? ClassifiedAdState.valueOf(status.toUpperCase())
              : null;
      List<ClassifiedAdQueryEntity> items =
          classifiedAdQueryController.findByOwnerPublished(ownerUuid, classifiedAdState);

      try {
        String result = objectMapper.writeValueAsString(items);
        response.status(200);
        return result;
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
      return null;
    };
  }

  public Route findByOwner() {
    return (req, res) -> {
      String owner = req.queryParams("owner");
      if (Strings.isNullOrEmpty(owner)) {
        var resMap = Map.of("status", false, "message", "owner is required as a query param");
        String s = objectMapper.writeValueAsString(resMap);
        res.status(404);
        return s;
      }
      List<ClassifiedAdQueryEntity> items =
          classifiedAdQueryController.findByOwner(UUID.fromString(owner));
      res.status(200);
      return serializeResponse(items);
    };
  }

  public Route findClassifiedAdEventsById() {
    return (request, response) -> {
      response.type("application/json");
      String classifiedAdId = getClassifiedIdFromRequest(request);
      Optional<ClassifiedAd> mayBe = classifiedAdService.findById(ClassifiedAdId.fromString(classifiedAdId));
      return mayBe
          .map(item -> {
            response.status(200);
            return serializeResponse(item.getChanges());
          })
          .orElseGet(
              () -> {
                response.status(404);
                return null;
              });
    };
  }
}
