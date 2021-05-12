package com.marketplace;

import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractTestInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTestInitializer.class);
//    protected ApplicationContext context;
//    protected DSLContext dslContext;

    @BeforeAll
    void setupAll() throws IOException {
//        System.out.println("BeforeAll lifecycle called");
//        ApplicationConfig config = ConfigLoader.loadClasspathResource("application.yml", ApplicationConfig.class);
//        context = DaggerApplicationContext.
//            builder()
//            .config(config)
//            .build();
//
//        dslContext = context.getDSLContext();
//
//        String dbConnectionUrl = config.getDb().getUrl();
//
//        FlywayMigration.migrate(dbConnectionUrl);
    }


}
