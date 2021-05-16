package com.marketplace;

import com.marketplace.config.ApplicationConfig;
import com.marketplace.config.ConfigLoader;
import com.marketplace.context.ApplicationContext;
import com.marketplace.context.DaggerApplicationContext;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdCommandRepository;
import com.marketplace.domain.classifiedad.service.ClassifiedAdService;
import com.marketplace.eventstore.jdbc.Tables;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseRepositoryTest extends AbstractContainerInitializer {

    ApplicationContext context;
    ClassifiedAdCommandRepository classifiedAdCommandRepository;
    ClassifiedAdService classifiedAdService;

    @BeforeEach
    void setup() throws IOException {
        ApplicationConfig config = ConfigLoader.loadClasspathResource("application.yml", ApplicationConfig.class);

        context = DaggerApplicationContext.
            builder()
            .config(config)
            .build();
        classifiedAdCommandRepository = context.getClassifiedAdRepository();
        classifiedAdService = context.getClassifiedAdService();

        dslContext.delete(Tables.CLASSIFIED_AD).execute();
        dslContext.delete(Tables.USER_PROFILE).execute();
    }

    @AfterEach
    public void cleanup() {
        dslContext.delete(Tables.CLASSIFIED_AD).execute();
        dslContext.delete(Tables.USER_PROFILE).execute();
    }

}
