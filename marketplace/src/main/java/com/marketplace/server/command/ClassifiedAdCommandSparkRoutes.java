package com.marketplace.server.command;

import static com.google.common.base.Predicates.instanceOf;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Patterns.$Failure;
import static io.vavr.Patterns.$Success;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.cqrs.command.CommandHandlerResult;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.command.CreateClassifiedAd;
import com.marketplace.domain.classifiedad.command.ImmutableUpdateClassifiedAd;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd;
import com.marketplace.domain.classifiedad.controller.CreateAdResponse;
import com.marketplace.domain.classifiedad.controller.ImmutableLoadClassifiedAdCommand;
import com.marketplace.domain.classifiedad.controller.LoadClassifiedAdCommand;
import com.marketplace.domain.classifiedad.controller.LoadClassifiedAdResponse;
import com.marketplace.domain.classifiedad.controller.UpdateClassifiedAdResponse;
import com.marketplace.domain.classifiedad.service.ClassifiedAdService;
import com.marketplace.domain.userprofile.controller.DuplicateDisplayNameException;
import com.marketplace.domain.userprofile.controller.LoadUserProfileResponse;
import com.marketplace.domain.userprofile.controller.command.ImmutableLoadUserProfileCommand;
import com.marketplace.domain.userprofile.controller.command.LoadUserProfileCommand;
import com.marketplace.server.ClassifiedAdBaseRoutes;
import io.vavr.API;
import io.vavr.control.Try;
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
    private final ObjectMapper objectMapper;
    private final ClassifiedAdService classifiedAdService;

    @Inject
    public ClassifiedAdCommandSparkRoutes(ObjectMapper objectMapper, ClassifiedAdService classifiedAdService) {
        super(objectMapper);
        this.objectMapper = objectMapper;
        this.classifiedAdService = classifiedAdService;
    }

    public Route getClassifiedAdRoute() {
        return ((req, res) -> {
            String classifiedAdId = getClassifiedIdFromRequest(req);
            LoadClassifiedAdCommand command = ImmutableLoadClassifiedAdCommand.builder()
                .classifiedAdId(UUID.fromString(classifiedAdId))
                .build();

            Try<LoadClassifiedAdResponse> tryResponse = classifiedAdService.handle(command);

            setJsonHeaders(res);
            return API.Match(tryResponse).of(
                Case($Success($()), value -> {
                    res.status(200);
                    return serializeResponse(value);
                }),
                Case($Failure($()), x -> {
                    res.status(404);
                    Map<String, Object> resMessage = Map.of(
                        "message", x.getMessage()
                    );
                    return serializeResponse(resMessage);
                })
            );
        });
    }
    public Route createClassifiedAdRoute() {
        return (request, response) -> {
            byte[] body = request.bodyAsBytes();
            CreateClassifiedAd createAdDto = objectMapper.readValue(body, CreateClassifiedAd.class);
            Try<CreateAdResponse> createAdResponse = classifiedAdService.handle(createAdDto);
            setJsonHeaders(response);

            return API.Match(createAdResponse).of(
                Case($Success($()), value -> {
                    response.status(201);
                    response.header("Location", String.format("/classified_ad/%s", value.getClassifiedAdId()));
                    return NO_CONTENT;
                }),
                Case($Failure($(instanceOf(DuplicateDisplayNameException.class))), ex -> {
                    response.status(400);
                    Map<String, Object> resMessage = Map.of(
                        "message", ex.getMessage()
                    );
                    return serializeResponse(resMessage);
                }),
                Case($Failure($()), x -> {
                    response.status(404);
                    Map<String, Object> resMessage = Map.of(
                        "message", x.getMessage()
                    );
                    return serializeResponse(resMessage);
                })
            );
        };
    }

    public Route updateClassifiedAd() {
        return (req, res) -> {
            String classifiedAdId = getClassifiedIdFromRequest(req);
            var updateClassifiedAdResponseTry = deserialize(req.bodyAsBytes(), UpdateClassifiedAd.class)
                .map(it -> ImmutableUpdateClassifiedAd.copyOf(it)
                    .withClassifiedAdId(UUID.fromString(classifiedAdId)))
                .flatMap(classifiedAdService::handle);

            return processResponse(res, updateClassifiedAdResponseTry);
        };
    }

    public Route updateClassifiedAdTitle() {
        return (req, res) -> {
            String classifiedAdId = getClassifiedIdFromRequest(req);
            var updateClassifiedAdResponseTry = deserialize(req.bodyAsBytes(), UpdateClassifiedAd.class)
                .map(it -> ImmutableUpdateClassifiedAd.copyOf(it)
                    .withClassifiedAdId(UUID.fromString(classifiedAdId)))
                .flatMap(classifiedAdService::handle);

            return processResponse(res, updateClassifiedAdResponseTry);
        };
    }

    public Route updateClassifiedAdOwner() {
        return (req, res) -> {
            String classifiedAdId = getClassifiedIdFromRequest(req);
            var updateClassifiedAdResponseTry = deserialize(req.bodyAsBytes(), UpdateClassifiedAd.class)
                .map(it -> ImmutableUpdateClassifiedAd.copyOf(it)
                    .withClassifiedAdId(UUID.fromString(classifiedAdId)))
                .flatMap(classifiedAdService::handle);

            return processResponse(res, updateClassifiedAdResponseTry);
        };
    }

    public Route updateClassifiedAdPrice() {
        return (req, res) -> {
            String classifiedAdId = getClassifiedIdFromRequest(req);
            var updateClassifiedAdResponseTry = deserialize(req.bodyAsBytes(), UpdateClassifiedAd.class)
                .map(it -> ImmutableUpdateClassifiedAd.copyOf(it)
                    .withClassifiedAdId(UUID.fromString(classifiedAdId)))
                .flatMap(classifiedAdService::handle);

            return processResponse(res, updateClassifiedAdResponseTry);
        };
    }

    public Route updateClassifiedAdText() {
        return (req, res) -> {
            String classifiedAdId = getClassifiedIdFromRequest(req);
            var updateClassifiedAdResponseTry = deserialize(req.bodyAsBytes(), UpdateClassifiedAd.class)
                .map(it -> ImmutableUpdateClassifiedAd.copyOf(it)
                    .withClassifiedAdId(UUID.fromString(classifiedAdId)))
                .flatMap(classifiedAdService::handle);

            return processResponse(res, updateClassifiedAdResponseTry);
        };
    }

    public Route approveClassifiedAd() {
        return (req, res) -> {
            String classifiedAdId = getClassifiedIdFromRequest(req);
            var updateClassifiedAdResponseTry = deserialize(req.bodyAsBytes(), UpdateClassifiedAd.class)
                .map(it -> ImmutableUpdateClassifiedAd.copyOf(it)
                    .withClassifiedAdId(UUID.fromString(classifiedAdId)))
                .flatMap(classifiedAdService::handle);

            return processResponse(res, updateClassifiedAdResponseTry);
        };
    }

    public Route publishClassifiedAd() {
        return (req, res) -> {
            String classifiedAdId = getClassifiedIdFromRequest(req);
            var commandResult = classifiedAdService
                .handle(ImmutableUpdateClassifiedAd
                    .builder()
                    .classifiedAdId(UUID.fromString(classifiedAdId))
                    .build());
            return processResponse(res, commandResult);
        };
    }

    private Object processResponse(Response res, Try<UpdateClassifiedAdResponse> updateClassifiedAdResponse) {
        setJsonHeaders(res);
        return API.Match(updateClassifiedAdResponse).of(
            Case($Success($()), value -> {
                res.status(200);
                Map<String, Object> resMessage = Map.of(
                    "status", true,
                    "message", "Successfully updated user",
                    "classified_ad_id", value.getId()
                );
                return serializeResponse(resMessage);
            }),
            Case($Failure($()), x -> {
                res.status(404);
                Map<String, Object> resMessage = Map.of(
                    "message", x.getMessage()
                );
                return serializeResponse(resMessage);
            })
        );
    }

    public Route addPictureToClassifiedAd() {
        return (req, res) -> {
//      String classifiedAdId = getClassifiedIdFromRequest(req);
//      var updateClassifiedAd = read(classifiedAdId, req);
//      return updateClassifiedAd.getPictures()
//          .map(pictureDtos -> {
//            var commandResult = classifiedAdController.addPictures(UUID.fromString(classifiedAdId), pictureDtos);
//            return processResponse(res, commandResult);
//          }).orElseGet(() -> {
//            Map<String, Object> resMessage = Map.of(
//                "status", false,
//                "message", "at least add a picture to be added to classifiedAd"
//            );
//            res.status(404);
//            return serializeResponse(resMessage);
//          });
            return null;
        };
    }

    public String serializeResponse(Object object) {
        return Try.of(() -> objectMapper.writeValueAsString(object))
            .onFailure(ex -> LOGGER.info("error while serializing object with message {}", ex.getMessage()))
            .getOrElse("");
    }

}
