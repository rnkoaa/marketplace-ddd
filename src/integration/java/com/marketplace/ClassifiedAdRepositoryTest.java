package com.marketplace;

import com.marketplace.context.mongo.MongoConfig;
import com.marketplace.context.mongo.MongoConfigModule;
import com.marketplace.controller.CreateAdDto;
import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.shared.UserId;
import com.marketplace.fixtures.LoadCreateAdEvent;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ClassifiedAdRepositoryTest extends AbstractContainerInitializer {
    MongoConfig mongoConfig;
    MongoClient mongoClient;
    MongoCollection<ClassifiedAd> classifiedAdCollection;

    @BeforeEach
    void setup() {
        String hosts = mongoDBContainer.getHost();
        int port = mongoDBContainer.getMappedPort(27017);
        mongoConfig = new MongoConfig(hosts, "test_db", port);
        mongoClient = MongoConfigModule.provideMongoClient(mongoConfig);
        classifiedAdCollection = MongoConfigModule.provideMongoDatabase(mongoClient, mongoConfig)
                .getCollection("classified_ad", ClassifiedAd.class);
    }

    @Test
    void validateCollectionConnects() throws IOException {
//        ApplicationRunner.
        CreateAdDto createAdDto = LoadCreateAdEvent.loadCreateAdDto();

        assertThat(createAdDto).isNotNull();
        assertThat(createAdDto.getOwnerId()).isNotNull();
        var classifiedAd = new ClassifiedAd(ClassifiedAdId.newClassifedAdId(),
                new UserId(createAdDto.getOwnerId()));


        InsertOneResult insertOneResult = classifiedAdCollection.insertOne(classifiedAd);

        assertThat(insertOneResult.wasAcknowledged()).isTrue();

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
