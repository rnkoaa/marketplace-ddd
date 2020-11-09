package com.marketplace.integration;

import com.marketplace.context.ObjectMapperModule;
import com.marketplace.controller.ClassifiedAdController;
import com.marketplace.controller.CreateAdDto;
import com.marketplace.controller.CreateAdResponse;
import com.marketplace.domain.ClassifiedAd;
import com.marketplace.domain.ClassifiedAdId;
import com.marketplace.domain.ClassifiedAdRepository;
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
        CreateAdDto createAdDto = LoadCreateAdEvent.loadCreateAdDto();

        assertThat(createAdDto).isNotNull();
        assertThat(createAdDto.getOwnerId()).isNotNull();

        var repository = ApplicationRunner.getBean(ClassifiedAdRepository.class);
        var controller = ApplicationRunner.getBean(ClassifiedAdController.class);
        assert controller != null;
        CreateAdResponse ad = controller.createAd(createAdDto);

        assertThat(ad).isNotNull();
        assertThat(ad.getId()).isNotNull();
        assertThat(ad.getOwnerId()).isNotNull().isEqualByComparingTo(createAdDto.getOwnerId());

        assert repository != null;
        Optional<ClassifiedAd> load = repository.load(new ClassifiedAdId(ad.getId()));
        assertThat(load).isPresent();

        ClassifiedAd classifiedAd = load.get();
        assertThat(classifiedAd.getChanges()).isNotNull().hasSize(3);

        String classifiedAdJson = ObjectMapperModule.objectMapper().writeValueAsString(classifiedAd);
        System.out.println(classifiedAdJson);

    }

    //
    @Test
    void classifiedAdCanBeSerializedAndDeserializedIntoJson() throws IOException {
        CreateAdDto createAdDto = LoadCreateAdEvent.loadCreateAdDto();

        assertThat(createAdDto).isNotNull();
        assertThat(createAdDto.getOwnerId()).isNotNull();

        var repository = ApplicationRunner.getBean(ClassifiedAdRepository.class);
        var controller = ApplicationRunner.getBean(ClassifiedAdController.class);
        assert controller != null;
        CreateAdResponse ad = controller.createAd(createAdDto);


        assert repository != null;
        Optional<ClassifiedAd> load = repository.load(new ClassifiedAdId(ad.getId()));
        assertThat(load).isPresent();

        ClassifiedAd classifiedAd = load.get();

        String classifiedAdJson = ObjectMapperModule.objectMapper().writeValueAsString(classifiedAd);

        var deserializedClassifiedAd = ObjectMapperModule.objectMapper().readValue(classifiedAdJson, ClassifiedAd.class);
        assertThat(deserializedClassifiedAd).isNotNull();
        assertThat(deserializedClassifiedAd.getChanges()).hasSize(3);
        deserializedClassifiedAd.getChanges().forEach(System.out::println);

    }
}
