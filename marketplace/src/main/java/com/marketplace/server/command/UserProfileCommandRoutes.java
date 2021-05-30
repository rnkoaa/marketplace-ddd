package com.marketplace.server.command;

import static com.google.common.base.Predicates.instanceOf;
import static com.marketplace.server.SparkServer.MEDIA_APPLICATION_JSON;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Patterns.$Failure;
import static io.vavr.Patterns.$Success;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.domain.userprofile.controller.CreateUserProfileCommand;
import com.marketplace.domain.userprofile.controller.CreateUserProfileResult;
import com.marketplace.domain.userprofile.controller.DuplicateDisplayNameException;
import com.marketplace.domain.userprofile.controller.ImmutableLoadUserProfileCommand;
import com.marketplace.domain.userprofile.controller.ImmutableUpdateUserFullNameCommand;
import com.marketplace.domain.userprofile.controller.ImmutableUpdateUserProfileCommand;
import com.marketplace.domain.userprofile.controller.LoadUserProfileCommand;
import com.marketplace.domain.userprofile.controller.LoadUserProfileResponse;
import com.marketplace.domain.userprofile.controller.UpdateUserProfileResult;
import com.marketplace.domain.userprofile.controller.UserProfileCommandService;
import com.marketplace.server.BaseSparkRoutes;
import io.vavr.API;
import io.vavr.control.Try;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import spark.Route;

@Named
@Singleton
public class UserProfileCommandRoutes extends BaseSparkRoutes {

    private final UserProfileCommandService commandService;

    @Inject
    public UserProfileCommandRoutes(ObjectMapper objectMapper,
        UserProfileCommandService commandService
    ) {
        super(objectMapper);
        this.commandService = commandService;
    }

    public Route createUserProfile() {
        return ((request, response) -> {
            Try<CreateUserProfileResult> createUserProfileResults = deserialize(request.bodyAsBytes(),
                CreateUserProfileCommand.class)
                .flatMap(commandService::handle);

            return API.Match(createUserProfileResults).of(
                Case($Success($()), value -> {
                    response.header(HEADER_CONTENT_TYPE, MEDIA_APPLICATION_JSON);
                    response.type(MEDIA_APPLICATION_JSON);
                    response.status(201);
                    response.header("Location", String.format("/user/%s", value.getId()));
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
        });
    }

    public Route updateUserFullName() {
        return ((req, response) -> {
            String userId = getRequestParam(req, "userId");
            TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
            };
            Try<Map<String, Object>> deserialize = deserialize(req.bodyAsBytes(), typeRef);
            Try<UpdateUserProfileResult> updateUserProfileResults = deserialize
                .map(res -> ImmutableUpdateUserFullNameCommand.builder()
                    .firstName((String) res.getOrDefault("first_name", ""))
                    .lastName((String) res.getOrDefault("last_name", ""))
                    .middleName((String) res.getOrDefault("middle_name", ""))
                    .userId(UUID.fromString(userId))
                    .build())
                .flatMap(commandService::handle);

            return API.Match(updateUserProfileResults).of(
                Case($Success($()), value -> {
                    response.header(HEADER_CONTENT_TYPE, MEDIA_APPLICATION_JSON);
                    response.type(MEDIA_APPLICATION_JSON);
                    response.status(200);
                    Map<String, Object> resMessage = Map.of(
                        "status", "Successfully updated user",
                        "user_id", value.getId()
                    );
                    return serializeResponse(resMessage);
                }),
                Case($Failure($()), x -> {
                    response.status(404);
                    response.header(HEADER_CONTENT_TYPE, MEDIA_APPLICATION_JSON);
                    response.type(MEDIA_APPLICATION_JSON);
                    Map<String, Object> resMessage = Map.of(
                        "message", x.getMessage()
                    );
                    return serializeResponse(resMessage);
                })
            );
        });
    }

    public Route updateUserProfile() {
        return ((req, response) -> {
            String userId = getRequestParam(req, "userId");
            TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
            };
            Try<Map<String, Object>> deserialize = deserialize(req.bodyAsBytes(), typeRef);
            Try<UpdateUserProfileResult> updateUserProfileResults = deserialize
                .map(res -> ImmutableUpdateUserProfileCommand.builder()
                    .firstName((String) res.getOrDefault("first_name", ""))
                    .lastName((String) res.getOrDefault("last_name", ""))
                    .middleName((String) res.getOrDefault("middle_name", ""))
                    .photoUrl((String) res.getOrDefault("photo_url", ""))
                    .displayName((String) res.getOrDefault("display_name", ""))
                    .userId(UUID.fromString(userId))
                    .build())
                .flatMap(commandService::handle);

            return API.Match(updateUserProfileResults).of(
                Case($Success($()), value -> {
                    response.header(HEADER_CONTENT_TYPE, MEDIA_APPLICATION_JSON);
                    response.type(MEDIA_APPLICATION_JSON);
                    response.status(200);
                    Map<String, Object> resMessage = Map.of(
                        "status", "Successfully updated user",
                        "user_id", value.getId()
                    );
                    return serializeResponse(resMessage);
                }),
                Case($Failure($()), x -> {
                    response.status(404);
                    response.header(HEADER_CONTENT_TYPE, MEDIA_APPLICATION_JSON);
                    response.type(MEDIA_APPLICATION_JSON);
                    Map<String, Object> resMessage = Map.of(
                        "message", x.getMessage()
                    );
                    return serializeResponse(resMessage);
                })
            );
        });
    }

    public Route updateUserProfilePhoto() {
        return ((req, response) -> {
            String userId = getRequestParam(req, "userId");
            TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
            };
            Try<Map<String, Object>> deserialize = deserialize(req.bodyAsBytes(), typeRef);
            Try<UpdateUserProfileResult> updateUserProfileResults = deserialize
                .map(res -> ImmutableUpdateUserProfileCommand.builder()
                    .photoUrl((String) res.getOrDefault("photo_url", ""))
                    .userId(UUID.fromString(userId))
                    .build())
                .flatMap(commandService::handle);

            return API.Match(updateUserProfileResults).of(
                Case($Success($()), value -> {
                    response.header(HEADER_CONTENT_TYPE, MEDIA_APPLICATION_JSON);
                    response.type(MEDIA_APPLICATION_JSON);
                    response.status(200);
                    Map<String, Object> resMessage = Map.of(
                        "status", "Successfully updated user",
                        "user_id", value.getId()
                    );
                    return serializeResponse(resMessage);
                }),
                Case($Failure($()), x -> {
                    response.status(404);
                    response.header(HEADER_CONTENT_TYPE, MEDIA_APPLICATION_JSON);
                    response.type(MEDIA_APPLICATION_JSON);
                    Map<String, Object> resMessage = Map.of(
                        "message", x.getMessage()
                    );
                    return serializeResponse(resMessage);
                })
            );
        });
    }

    public Route updateUserDisplayName() {
        return null;
    }

    public Route getUserProfile() {
       return ((req, res) -> {
           String userId = getRequestParam(req, "userId");
           LoadUserProfileCommand command = ImmutableLoadUserProfileCommand.builder()
               .userId(UUID.fromString(userId))
               .build();

           Try<LoadUserProfileResponse> tryResponse = commandService.handle(command);

           return API.Match(tryResponse).of(
               Case($Success($()), value -> {
                   res.header(HEADER_CONTENT_TYPE, MEDIA_APPLICATION_JSON);
                   res.type(MEDIA_APPLICATION_JSON);
                   res.status(200);
                   return serializeResponse(value);
               }),
               Case($Failure($()), x -> {
                   res.status(404);
                   res.header(HEADER_CONTENT_TYPE, MEDIA_APPLICATION_JSON);
                   res.type(MEDIA_APPLICATION_JSON);
                   Map<String, Object> resMessage = Map.of(
                       "message", x.getMessage()
                   );
                   return serializeResponse(resMessage);
               })
           );
       }) ;
    }
}
