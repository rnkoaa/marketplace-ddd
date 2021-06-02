package com.marketplace;

import com.marketplace.config.ApplicationConfig;
import com.marketplace.config.ConfigLoader;
import com.marketplace.context.ApplicationContext;
import com.marketplace.context.DaggerApplicationContext;
import com.marketplace.domain.AggregateStoreRepository;
import com.marketplace.domain.classifiedad.service.ClassifiedAdService;
import com.marketplace.domain.userprofile.UserProfileEventListener;
import com.marketplace.evenstore.jooq.Tables;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseRepositoryTest extends AbstractContainerInitializer {

    ApplicationContext context;
    AggregateStoreRepository aggregateStoreRepository;
    ClassifiedAdService classifiedAdService;
    protected UserProfileEventListener userProfileEventListener;

    @BeforeEach
    void setup() throws IOException {
        ApplicationConfig config = ConfigLoader.loadClasspathResource("application.yml", ApplicationConfig.class);

        context = DaggerApplicationContext.
            builder()
            .config(config)
            .build();
        aggregateStoreRepository = context.getAggregateRepository();
        classifiedAdService = context.getClassifiedAdService();
        userProfileEventListener = context.getUserProfileEventListener();

        dslContext.delete(Tables.CLASSIFIED_AD).execute();
        dslContext.delete(Tables.USER_PROFILE).execute();
    }

    @AfterEach
    public void cleanup() {
        dslContext.delete(Tables.EVENT_DATA).execute();
        dslContext.delete(Tables.CLASSIFIED_AD).execute();
        dslContext.delete(Tables.USER_PROFILE).execute();
    }

}
