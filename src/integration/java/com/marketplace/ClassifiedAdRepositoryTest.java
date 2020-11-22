package com.marketplace;

import com.marketplace.config.ApplicationConfig;
import com.marketplace.config.ConfigLoader;
import com.marketplace.context.ApplicationContext;
import com.marketplace.context.DaggerApplicationContext;
import com.marketplace.context.mongo.MongoConfig;
import com.marketplace.context.mongo.MongoConfigModule;
import com.marketplace.domain.classifiedad.controller.CreateAdDto;
import com.marketplace.domain.PictureSize;
import com.marketplace.domain.classifiedad.*;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdRepository;
import com.marketplace.domain.shared.UserId;
import com.marketplace.fixtures.LoadCreateAdEvent;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
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
        classifiedAd.updateTitle(new ClassifiedAdTitle("Snow Blower for sale"));
        classifiedAd.updateText(new ClassifiedAdText("Snow Blower for sale for Cheap"));
        classifiedAd.updatePrice(new Price(Money.fromDecimal(4.59, "USD")));
        classifiedAd.addPicture("uri", new PictureSize(800, 600), 0);
        classifiedAd.setState(ClassifiedAdState.active);

        ClassifiedAd savedClassifiedAd = classifiedAdRepository.add(classifiedAd);

        assertThat(savedClassifiedAd.getId()).isNotNull();
        assertThat(savedClassifiedAd.getId().id()).isNotNull()
                .isEqualByComparingTo(UUID.fromString(insertId));

        Optional<ClassifiedAd> load = classifiedAdRepository.load(ClassifiedAdId.fromString(insertId));

        assertThat(load).isPresent();
        ClassifiedAd found = load.get();
        assertThat(found.getId()).isEqualTo(ClassifiedAdId.fromString(insertId));
        assertThat(found.getOwnerId()).isEqualTo(new UserId(createAdDto.getOwnerId()));
        assertThat(found.getText()).isEqualTo(new ClassifiedAdText("Snow Blower for sale for Cheap"));
        assertThat(found.getTitle()).isEqualTo(new ClassifiedAdTitle("Snow Blower for sale"));
        assertThat(found.getPrice()).isEqualTo(new Price(Money.fromDecimal(4.59, "USD")));
        assertThat(found.getPictures()).hasSize(1);
        assertThat(found.getChanges()).hasSameSizeAs(classifiedAd.getChanges());
    }
}
