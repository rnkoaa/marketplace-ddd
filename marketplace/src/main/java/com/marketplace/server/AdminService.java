package com.marketplace.server;

import static com.marketplace.server.SparkServer.MEDIA_APPLICATION_JSON;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.domain.userprofile.controller.UserProfileCommandService;
import com.marketplace.domain.userprofile.controller.command.ImmutableDeleteAllUsersCommand;
import com.marketplace.domain.userprofile.repository.UserProfileQueryRepository;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import spark.Spark;

@Named
@Singleton
public class AdminService extends BaseSparkRoutes {

    private final UserProfileQueryRepository userProfileQueryRepository;
    private final UserProfileCommandService userProfileCommandService;

    @Inject
    public AdminService(ObjectMapper objectMapper,
        UserProfileQueryRepository userProfileQueryRepository,
        UserProfileCommandService userProfileCommandService) {
        super(objectMapper);
        this.userProfileQueryRepository = userProfileQueryRepository;
        this.userProfileCommandService = userProfileCommandService;
    }

    public void register(String baseEndpoint) {
        // load all events for an aggregate
        Spark.delete(String.format("%s/user", baseEndpoint), MEDIA_APPLICATION_JSON, ((request, response) -> {
            setJsonHeaders(response);
            userProfileQueryRepository.deleteAll();
            userProfileCommandService.handle(ImmutableDeleteAllUsersCommand.builder().build());
            return "";
        }));

    }
}
