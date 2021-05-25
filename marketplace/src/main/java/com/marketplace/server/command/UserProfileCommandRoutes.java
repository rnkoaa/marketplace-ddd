package com.marketplace.server.command;

import static com.marketplace.server.SparkServer.MEDIA_APPLICATION_JSON;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Patterns.$Failure;
import static io.vavr.Patterns.$Success;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.domain.userprofile.controller.CreateUserProfileCommand;
import com.marketplace.domain.userprofile.controller.CreateUserProfileResult;
import com.marketplace.domain.userprofile.controller.UserProfileCommandService;
import com.marketplace.server.BaseSparkRoutes;
import io.vavr.API;
import io.vavr.control.Try;
import java.util.Map;
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
                    return response;
                }),
                Case($Failure($()), x -> {
                    response.status(404);
                    Map<String, Object> resMessage = Map.of(
                        "status", false,
                        "message", x.getMessage()
                    );
                    response.body(serializeResponse(resMessage));
                    return response;
                })
            );
        });
    }

}
