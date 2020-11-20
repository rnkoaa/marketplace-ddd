package com.marketplace;

import com.marketplace.config.ApplicationConfig;
import com.marketplace.config.ConfigLoader;
import com.marketplace.context.ApplicationContext;
import com.marketplace.context.DaggerApplicationContext;
import com.marketplace.context.mongo.MongoConfig;
import com.marketplace.context.mongo.MongoConfigModule;
import com.marketplace.controller.CreateAdDto;
import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdRepository;
import com.marketplace.domain.shared.UserId;
import com.marketplace.fixtures.LoadCreateAdEvent;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ClassifiedAdRepositoryTest extends AbstractContainerInitializer {

    String insertId = "0b8a557d-32f6-4268-80d5-6a38df8a9520";
    MongoConfig mongoConfig;
    MongoClient mongoClient;
    MongoCollection<ClassifiedAd> classifiedAdCollection;
    ApplicationContext context;
    ClassifiedAdRepository classifiedAdRepository;

    @BeforeEach
    void setup() throws IOException {
        ApplicationConfig config = ConfigLoader.loadClasspathResource("application.yml", ApplicationConfig.class);

        context = DaggerApplicationContext.
                builder()
                .config(config)
                .build();

//        context.
        String hosts = mongoDBContainer.getHost();
        int port = mongoDBContainer.getMappedPort(27017);
        mongoConfig = new MongoConfig(hosts, "test_db", port);
        config.setMongoConfig(mongoConfig);
        mongoClient = MongoConfigModule.provideMongoClient(mongoConfig);
        classifiedAdCollection = MongoConfigModule.provideMongoDatabase(mongoClient, mongoConfig)
                .getCollection(ClassifiedAd.class.getSimpleName().toLowerCase(), ClassifiedAd.class);
        classifiedAdRepository = context.getClassifiedAdRepository();
    }

    @AfterEach
    public void cleanup() {
        DeleteResult deleteResult = classifiedAdCollection.deleteOne(eq("_id", insertId));
        if (deleteResult.wasAcknowledged()) {
            System.out.println("delete was acknowledged.");
        }
        System.out.println("Number of records deleted: " + deleteResult.getDeletedCount());

    }

    @Test
    void insertedItemCanBeShownToExist() throws IOException {
        CreateAdDto createAdDto = LoadCreateAdEvent.loadCreateAdDto();

        assertThat(createAdDto).isNotNull();
        assertThat(createAdDto.getOwnerId()).isNotNull();
        var classifiedAd = new ClassifiedAd(ClassifiedAdId.fromString(insertId),
                new UserId(createAdDto.getOwnerId()));

        InsertOneResult insertOneResult = classifiedAdCollection.insertOne(classifiedAd);

        assertThat(insertOneResult.wasAcknowledged()).isTrue();
        assertThat(insertOneResult.getInsertedId()).isNotNull();
        assertThat(insertOneResult.getInsertedId().asString())
                .isNotNull();

        assertThat(insertOneResult.getInsertedId().asString().getValue())
                .isNotBlank()
                .isEqualTo(insertId);

        ClassifiedAd savedClassifiedAd = classifiedAdCollection.find(eq("_id", insertId)).first();
        assertThat(savedClassifiedAd).isNotNull();
        System.out.println(savedClassifiedAd.toString());

        assertThat(savedClassifiedAd.getId()).isNotNull();
        assertThat(savedClassifiedAd.getId().id()).isNotNull()
                .isEqualByComparingTo(UUID.fromString(insertId));

        Optional<ClassifiedAd> load = classifiedAdRepository.load(ClassifiedAdId.fromString(insertId));

        assertThat(load).isPresent();
    }

    @Test
    void validateCollectionConnects() throws IOException {
        String insertId = "0b8a557d-32f6-4268-80d5-6a38df8a9520";
        CreateAdDto createAdDto = LoadCreateAdEvent.loadCreateAdDto();

        assertThat(createAdDto).isNotNull();
        assertThat(createAdDto.getOwnerId()).isNotNull();
        var classifiedAd = new ClassifiedAd(ClassifiedAdId.fromString(insertId),
                new UserId(createAdDto.getOwnerId()));

        InsertOneResult insertOneResult = classifiedAdCollection.insertOne(classifiedAd);

        assertThat(insertOneResult.wasAcknowledged()).isTrue();
        assertThat(insertOneResult.getInsertedId()).isNotNull();
        assertThat(insertOneResult.getInsertedId().asString())
                .isNotNull();

        assertThat(insertOneResult.getInsertedId().asString().getValue())
                .isNotBlank()
                .isEqualTo(insertId);


//        var repository = ApplicationRunner.getBean(ClassifiedAdRepository.class);
//        var controller = ApplicationRunner.getBean(ClassifiedAdController.class);
//        assert controller != null;
//        CreateAdResponse ad = controller.createAd(createAdDto);

//        AssertionsForClassTypes.assertThat(ad).isNotNull();
//        assertThat(ad.getId()).isNotNull();
//        assertThat(ad.getOwnerId()).isNotNull().isEqualByComparingTo(createAdDto.getOwnerId());
//
//        assert repository != null;
//        Optional<ClassifiedAd> load = repository.load(new ClassifiedAdId(ad.getId()));
//        AssertionsForClassTypes.assertThat(load).isPresent();
//
//        ClassifiedAd classifiedAd = load.get();
//        assertThat(classifiedAd.getChanges()).isNotNull().hasSize(3);
//
//        String classifiedAdJson = ObjectMapperModule.objectMapper().writeValueAsString(classifiedAd);
//        System.out.println(classifiedAdJson);
//        assertThat(classifiedAdCollection != null).isNotNull();
    }

}
