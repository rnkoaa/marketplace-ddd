package com.marketplace;

import com.marketplace.config.ApplicationConfig;
import com.marketplace.config.ConfigLoader;
import com.marketplace.context.ApplicationContext;
import com.marketplace.context.DaggerApplicationContext;
import java.io.IOException;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractContainerInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractContainerInitializer.class);
    protected ApplicationContext context;
    protected DSLContext dslContext;

    @BeforeAll
    void setupAll() throws IOException {
        ApplicationConfig config = ConfigLoader.loadClasspathResource("application.yml", ApplicationConfig.class);
        context = DaggerApplicationContext.
            builder()
            .config(config)
            .build();

        dslContext = context.getDSLContext();

        String dbConnectionUrl = config.getDb().getUrl();
    }

    public ApplicationContext getApplicationContext() {
        return context;
    }

}
