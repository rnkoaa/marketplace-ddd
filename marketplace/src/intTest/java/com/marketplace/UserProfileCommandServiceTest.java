package com.marketplace;

import static com.marketplace.evenstore.jooq.Tables.USER_PROFILE;
import static com.marketplace.eventstore.jdbc.Tables.EVENT_DATA;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.google.common.eventbus.EventBus;
import com.marketplace.config.ApplicationConfig;
import com.marketplace.config.ConfigLoader;
import com.marketplace.context.ApplicationContext;
import com.marketplace.context.DaggerApplicationContext;
import com.marketplace.domain.AggregateStoreRepository;
import com.marketplace.domain.userprofile.UserProfileEventListener;
import com.marketplace.domain.userprofile.controller.CreateUserProfileResult;
import com.marketplace.domain.userprofile.controller.DuplicateDisplayNameException;
import com.marketplace.domain.userprofile.controller.UpdateUserFullNameCommand;
import com.marketplace.domain.userprofile.controller.UpdateUserProfileResult;
import com.marketplace.domain.userprofile.controller.UserProfileCommandService;
import com.marketplace.domain.userprofile.entity.UserProfileEntity;
import com.marketplace.domain.userprofile.repository.UserProfileQueryRepository;
import com.marketplace.fixtures.LoadUpdateUserFullName;
import com.marketplace.fixtures.UserProfileFixture;
import io.vavr.control.Try;
import java.io.IOException;
import java.util.Optional;
import org.jooq.DSLContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("UnstableApiUsage")
public class UserProfileCommandServiceTest extends AbstractContainerInitializer {

    String insertId = "0b8a557d-32f6-4268-80d5-6a38df8a9520";
    ApplicationContext context;
    DSLContext dslContext;
    AggregateStoreRepository aggregateStoreRepository;

    private UserProfileCommandService userProfileCommandService;

    private UserProfileEventListener userProfileEventListener;

    private UserProfileQueryRepository userProfileQueryRepository;

    EventBus eventBus;

    @BeforeEach
    void setup() throws IOException {
        ApplicationConfig config =
            ConfigLoader.loadClasspathResource("application.yml", ApplicationConfig.class);

        context = DaggerApplicationContext.builder().config(config).build();
        dslContext = context.getDSLContext();
        aggregateStoreRepository = context.getAggregateRepository();
        userProfileCommandService = context.getUsProfileCommandService();
        userProfileEventListener = context.getUserProfileEventListener();
        eventBus = context.getEventBus();

        userProfileQueryRepository = context.getUserProfileQueryRepository();

        eventBus.register(userProfileEventListener);
        cleanup();
    }

    @Test
    void testContextLoads() {
        assertThat(userProfileCommandService).isNotNull();
    }

    @Test
    void testNewUserCanBeCreatedAndSaved() throws IOException {
        var userProfileCommand = UserProfileFixture.loadCreateUserProfileDto();
        Try<CreateUserProfileResult> handle = userProfileCommandService.handle(userProfileCommand);
        assertThat(handle.isSuccess()).isTrue();
    }

    @Test
    void testSameUserCannotBeRegisteredTwice() throws IOException {
        var userProfileCommand = UserProfileFixture.loadCreateUserProfileDto();
        assertThat(userProfileCommandService.handle(userProfileCommand).isSuccess()).isTrue();

        Try<CreateUserProfileResult> secondTry = userProfileCommandService.handle(userProfileCommand);
        assertThat(secondTry.isSuccess()).isFalse();
        assertThat(secondTry.getCause()).isInstanceOf(DuplicateDisplayNameException.class);
    }

    @Test
    void testUserFullNameCanBeUpdatedForExisingUser() throws IOException {
        var userProfileCommand = UserProfileFixture.loadCreateUserProfileDto();
        Try<CreateUserProfileResult> createUserProfileResults = userProfileCommandService.handle(userProfileCommand);
        assertThat(createUserProfileResults.isSuccess()).isTrue();

        CreateUserProfileResult createUserProfileResult = createUserProfileResults.get();
        UpdateUserFullNameCommand updateUserFullNameCommand = LoadUpdateUserFullName
            .load(createUserProfileResult.getId());

        Try<UpdateUserProfileResult> updateUserProfileResults = userProfileCommandService
            .handle(updateUserFullNameCommand);
        assertThat(updateUserProfileResults.isSuccess()).isTrue();

        Optional<UserProfileEntity> mayBeUserProfileEntity = userProfileQueryRepository
            .findById(createUserProfileResult.getId());

        assertThat(mayBeUserProfileEntity).isPresent();

        UserProfileEntity entity = mayBeUserProfileEntity.get();
        assertThat(entity.getFirstName()).isEqualTo(updateUserFullNameCommand.fullName().firstName());
        assertThat(entity.getLastName()).isEqualTo(updateUserFullNameCommand.fullName().lastName());
    }

    @AfterEach
    public void cleanup() {
        dslContext.delete(EVENT_DATA).execute();
        dslContext.delete(USER_PROFILE).execute();
    }

}
