package com.marketplace.integration;

import com.marketplace.context.ObjectMapperModule;
import com.marketplace.domain.classifiedad.command.CreateClassifiedAd;
import com.marketplace.domain.classifiedad.controller.ClassifiedAdController;
import com.marketplace.domain.classifiedad.controller.CreateAdResponse;
import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdRepository;
import com.marketplace.fixtures.ApplicationRunner;
import com.marketplace.fixtures.LoadCreateAdEvent;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class CreateClassifiedAdTest {
    //
    @Test
    void classifiedAdCanBeCreated() throws IOException {
        CreateClassifiedAd createAdDto = LoadCreateAdEvent.loadCreateAdDto();

        assertThat(createAdDto).isNotNull();
        assertThat(createAdDto.getOwnerId()).isNotNull();

        var repository = ApplicationRunner.getBean(ClassifiedAdRepository.class);
        var controller = ApplicationRunner.getBean(ClassifiedAdController.class);
        assert controller != null;
        var ad = controller.createAd(createAdDto);

        assertThat(ad.result).isNotNull();
        assertThat(ad.result.getId()).isNotNull();
        assertThat(ad.result.getOwnerId()).isNotNull().isEqualByComparingTo(createAdDto.getOwnerId());

        assert repository != null;
        Optional<ClassifiedAd> load = repository.load(new ClassifiedAdId(ad.result.getId()));
        assertThat(load).isPresent();

        ClassifiedAd classifiedAd = load.get();
        assertThat(classifiedAd.getChanges()).isNotNull().hasSize(3);

        String classifiedAdJson = ObjectMapperModule.provideObjectMapper().writeValueAsString(classifiedAd);
    }

    //
    @Test
    void classifiedAdCanBeSerializedAndDeserializedIntoJson() throws IOException {
        CreateClassifiedAd createAdDto = LoadCreateAdEvent.loadCreateAdDto();

        assertThat(createAdDto).isNotNull();
        assertThat(createAdDto.getOwnerId()).isNotNull();

        var repository = ApplicationRunner.getBean(ClassifiedAdRepository.class);
        var controller = ApplicationRunner.getBean(ClassifiedAdController.class);
        assert controller != null;
        var ad = controller.createAd(createAdDto);


        assert repository != null;
        Optional<ClassifiedAd> load = repository.load(new ClassifiedAdId(ad.result.getId()));
        assertThat(load).isPresent();

        ClassifiedAd classifiedAd = load.get();

        String classifiedAdJson = ObjectMapperModule.provideObjectMapper().writeValueAsString(classifiedAd);

        var deserializedClassifiedAd = ObjectMapperModule.provideObjectMapper().readValue(classifiedAdJson, ClassifiedAd.class);
        assertThat(deserializedClassifiedAd).isNotNull();
        assertThat(deserializedClassifiedAd.getChanges()).hasSize(3);
        deserializedClassifiedAd.getChanges().forEach(System.out::println);
    }
}
