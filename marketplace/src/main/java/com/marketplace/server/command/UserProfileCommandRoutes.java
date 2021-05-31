package com.marketplace.server.command;

import static com.google.common.base.Predicates.instanceOf;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Patterns.$Failure;
import static io.vavr.Patterns.$Success;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.domain.userprofile.controller.CreateUserProfileResult;
import com.marketplace.domain.userprofile.controller.DuplicateDisplayNameException;
import com.marketplace.domain.userprofile.controller.LoadUserProfileResponse;
import com.marketplace.domain.userprofile.controller.UpdateUserProfileResult;
import com.marketplace.domain.userprofile.controller.UserProfileCommandService;
import com.marketplace.domain.userprofile.controller.command.CreateUserProfileCommand;
import com.marketplace.domain.userprofile.controller.command.ImmutableLoadUserProfileCommand;
import com.marketplace.domain.userprofile.controller.command.ImmutableUpdateUserFullNameCommand;
import com.marketplace.domain.userprofile.controller.command.ImmutableUpdateUserProfileCommand;
import com.marketplace.domain.userprofile.controller.command.LoadUserProfileCommand;
import com.marketplace.domain.userprofile.controller.command.UpdateUserFullNameCommand;
import com.marketplace.domain.userprofile.controller.command.UpdateUserProfileCommand;
import com.marketplace.server.BaseSparkRoutes;
import io.vavr.API;
import io.vavr.control.Try;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import spark.Response;
import spark.Route;

@Named
@Singleton
public class UserProfileCommandRoutes extends BaseSparkRoutes {

    private final UserProfileCommandService commandService;

    @Inject
    public UserProfileCommandRoutes(ObjectMapper objectMapper, UserProfileCommandService commandService) {
        super(objectMapper);
        this.commandService = commandService;
    }

    public Route createUserProfile() {
        return ((request, response) -> {
            Try<CreateUserProfileResult> createUserProfileResults = deserialize(request.bodyAsBytes(),
                CreateUserProfileCommand.class)
                .flatMap(commandService::handle);

            setJsonHeaders(response);

            return API.Match(createUserProfileResults).of(
                Case($Success($()), value -> {
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
        return ((req, res) -> {
            String userId = getRequestParam(req, "userId");
            TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
            };
            Try<Map<String, Object>> deserialize = deserialize(req.bodyAsBytes(), typeRef);
            Try<UpdateUserProfileResult> updateUserProfileResults = deserialize
                .map(result -> fullNameCommand(userId, result))
                .flatMap(commandService::handle);
            return generateResponse(res, updateUserProfileResults);
        });
    }

    public Route updateUserProfile() {
        return ((req, res) -> {
            String userId = getRequestParam(req, "userId");
            TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
            };
            Try<Map<String, Object>> deserialize = deserialize(req.bodyAsBytes(), typeRef);
            Try<UpdateUserProfileResult> updateUserProfileResults = deserialize
                .map(result -> updateUserProfileCommand(userId, result))
                .flatMap(commandService::handle);

            return generateResponse(res, updateUserProfileResults);
        });
    }

    public Route updateUserProfilePhoto() {
        return ((req, res) -> {
            String userId = getRequestParam(req, "userId");
            TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
            };
            Try<Map<String, Object>> deserialize = deserialize(req.bodyAsBytes(), typeRef);
            Try<UpdateUserProfileResult> updateUserProfileResults = deserialize
                .map(result -> photoUrlCommand(userId, result))
                .flatMap(commandService::handle);
            return generateResponse(res, updateUserProfileResults);
        });
    }

    public Route updateUserDisplayName() {
        return ((req, res) -> {
            String userId = getRequestParam(req, "userId");
            TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
            };
            Try<Map<String, Object>> deserialize = deserialize(req.bodyAsBytes(), typeRef);
            Try<UpdateUserProfileResult> updateUserProfileResults = deserialize
                .map(result -> displayNameCommand(userId, result))
                .flatMap(commandService::handle);

            return generateResponse(res, updateUserProfileResults);
        });
    }

    public Route getUserProfile() {
        return ((req, res) -> {
            String userId = getRequestParam(req, "userId");
            LoadUserProfileCommand command = ImmutableLoadUserProfileCommand.builder()
                .userId(UUID.fromString(userId))
                .build();

            Try<LoadUserProfileResponse> tryResponse = commandService.handle(command);

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

    private Object generateResponse(Response res, Try<UpdateUserProfileResult> updateUserProfileResults) {
        setJsonHeaders(res);
        return API.Match(updateUserProfileResults).of(
            Case($Success($()), value -> {
                res.status(200);
                Map<String, Object> resMessage = Map.of(
                    "status", "Successfully updated user",
                    "user_id", value.getId()
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

    private static UpdateUserProfileCommand photoUrlCommand(String userId, Map<String, Object> input) {
        return ImmutableUpdateUserProfileCommand.builder()
            .photoUrl((String) input.getOrDefault("photo_url", ""))
            .userId(UUID.fromString(userId))
            .build();
    }

    private static UpdateUserFullNameCommand fullNameCommand(String userId, Map<String, Object> input) {
        return ImmutableUpdateUserFullNameCommand.builder()
            .firstName((String) input.getOrDefault("first_name", ""))
            .lastName((String) input.getOrDefault("last_name", ""))
            .middleName((String) input.getOrDefault("middle_name", ""))
            .userId(UUID.fromString(userId))
            .build();
    }

    private static UpdateUserProfileCommand updateUserProfileCommand(String userId, Map<String, Object> input) {
        return ImmutableUpdateUserProfileCommand.builder()
            .firstName((String) input.getOrDefault("first_name", ""))
            .lastName((String) input.getOrDefault("last_name", ""))
            .middleName((String) input.getOrDefault("middle_name", ""))
            .photoUrl((String) input.getOrDefault("photo_url", ""))
            .displayName((String) input.getOrDefault("display_name", ""))
            .userId(UUID.fromString(userId))
            .build();
    }

    private static UpdateUserProfileCommand displayNameCommand(String userId, Map<String, Object> input) {
        return ImmutableUpdateUserProfileCommand.builder()
            .displayName((String) input.getOrDefault("display_name", ""))
            .userId(UUID.fromString(userId))
            .build();
    }
}
