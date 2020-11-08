package com.marketplace.integration;

import com.marketplace.controller.ClassifiedAdController;
import com.marketplace.controller.CreateAdDto;
import com.marketplace.controller.CreateAdResponse;
import com.marketplace.controller.UpdateAdDto;
import com.marketplace.domain.ClassifiedAd;
import com.marketplace.domain.ClassifiedAdId;
import com.marketplace.domain.ClassifiedAdRepository;
import com.marketplace.fixtures.ApplicationRunner;
import com.marketplace.fixtures.LoadCreateAdEvent;
import com.marketplace.fixtures.LoadUpdateAdEvent;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class UpdateClassifiedAdTest {
    //
    @Test
    void classifiedAdCanBeCreatedAndUpdated() throws IOException {
        CreateAdDto createAdDto = LoadCreateAdEvent.loadCreateAdDto();

        assertThat(createAdDto).isNotNull();
        assertThat(createAdDto.getOwnerId()).isNotNull();

        var repository = ApplicationRunner.getBean(ClassifiedAdRepository.class);
        var controller = ApplicationRunner.getBean(ClassifiedAdController.class);
        assert controller != null;
        CreateAdResponse ad = controller.createAd(createAdDto);

        assertThat(ad).isNotNull();

        UpdateAdDto updateAdDto = LoadUpdateAdEvent.load();

        // id for ad is generated dynamically.
        updateAdDto.setId(ad.getId());
        controller.updateClassifiedAd(updateAdDto);

        assert repository != null;
        Optional<ClassifiedAd> load = repository.load(new ClassifiedAdId(ad.getId()));
        assertThat(load).isPresent();

        ClassifiedAd classifiedAd = load.get();
        assertThat(classifiedAd.getChanges()).isNotNull().hasSize(5);

        assertThat(classifiedAd.getText().toString()).startsWith("update");
        assertThat(classifiedAd.getTitle().toString()).startsWith("update");

    }
}
