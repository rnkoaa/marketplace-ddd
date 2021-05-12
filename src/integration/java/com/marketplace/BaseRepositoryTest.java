package com.marketplace;

import com.marketplace.config.ApplicationConfig;
import com.marketplace.config.ConfigLoader;
import com.marketplace.context.ApplicationContext;
import com.marketplace.context.DaggerApplicationContext;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdCommandRepository;
import com.marketplace.domain.classifiedad.service.ClassifiedAdService;
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
    }

    @AfterEach
    public void cleanup() {
//    DeleteResult deleteResult = classifiedAdCollection.deleteMany(new Document());
//    if (deleteResult.wasAcknowledged()) {
//      System.out.println("delete was acknowledged.");
//    }
//    System.out.println("Number of records deleted: " + deleteResult.getDeletedCount());
    }

}
