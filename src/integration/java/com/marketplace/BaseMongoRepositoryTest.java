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

public abstract class BaseMongoRepositoryTest extends AbstractContainerInitializer {

    //  MongoClient mongoClient;
//  MongoCollection<ClassifiedAd> classifiedAdCollection;
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

        String hosts = mongoDBContainer.getHost();
        int port = mongoDBContainer.getMappedPort(27017);
////    mongoClient = MongoConfigModule.provideMongoClient(mongoConfig);
////    classifiedAdCollection = MongoConfigModule.provideMongoDatabase(mongoClient, mongoConfig)
//        .getCollection(ClassifiedAd.class.getSimpleName().toLowerCase(), ClassifiedAd.class);
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
