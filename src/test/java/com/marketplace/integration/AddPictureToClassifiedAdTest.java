package com.marketplace.integration;

import com.marketplace.domain.classifiedad.command.CreateClassifiedAd;
import com.marketplace.domain.classifiedad.controller.AddPictureToClassifiedAd;
import com.marketplace.domain.classifiedad.controller.ClassifiedAdController;
import com.marketplace.domain.classifiedad.controller.CreateAdResponse;
import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdRepository;
import com.marketplace.fixtures.ApplicationRunner;
import com.marketplace.fixtures.LoadAddPicture;
import com.marketplace.fixtures.LoadCreateAdEvent;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class AddPictureToClassifiedAdTest {
    //
    @Test
    void classifiedAdCanBeCreatedAndAPictureCanBeAdded() throws IOException {
        CreateClassifiedAd createAdDto = LoadCreateAdEvent.loadCreateAdDto();

        assertThat(createAdDto).isNotNull();
        assertThat(createAdDto.getOwnerId()).isNotNull();

        var repository = ApplicationRunner.getBean(ClassifiedAdRepository.class);
        var controller = ApplicationRunner.getBean(ClassifiedAdController.class);
        assert controller != null;
        var ad = controller.createAd(createAdDto);

        assertThat(ad.result).isNotNull();
        assertThat(ad.getResult().getId()).isNotNull();
        assertThat(ad.getResult().getOwnerId()).isNotNull().isEqualByComparingTo(createAdDto.getOwnerId());

        AddPictureToClassifiedAd addPictureToClassifiedAd = LoadAddPicture.load();
        addPictureToClassifiedAd.setId(ad.result.getId());
        controller.addPicture(addPictureToClassifiedAd);

        assert repository != null;
        Optional<ClassifiedAd> load = repository.load(new ClassifiedAdId(ad.result.getId()));
        assertThat(load).isPresent();

        ClassifiedAd classifiedAd = load.get();
        assertThat(classifiedAd.getChanges()).isNotNull().hasSize(4);
        assertThat(classifiedAd.getPictures()).hasSize(1);

    }
}
