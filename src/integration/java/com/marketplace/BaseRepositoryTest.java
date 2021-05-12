package com.marketplace;

import com.marketplace.domain.classifiedad.repository.ClassifiedAdCommandRepository;
import com.marketplace.domain.classifiedad.service.ClassifiedAdService;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseRepositoryTest extends AbstractTestInitializer {

    //  MongoClient mongoClient;
//  MongoCollection<ClassifiedAd> classifiedAdCollection;

    ClassifiedAdCommandRepository classifiedAdCommandRepository;
    ClassifiedAdService classifiedAdService;

    @BeforeEach
    void setup() throws IOException {


////    mongoClient = MongoConfigModule.provideMongoClient(mongoConfig);
////    classifiedAdCollection = MongoConfigModule.provideMongoDatabase(mongoClient, mongoConfig)
//        .getCollection(ClassifiedAd.class.getSimpleName().toLowerCase(), ClassifiedAd.class);
//        classifiedAdCommandRepository = context.getClassifiedAdRepository();
//        classifiedAdService = context.getClassifiedAdService();
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
