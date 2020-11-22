package com.marketplace.integration;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.controller.*;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdRepository;
import com.marketplace.fixtures.ApplicationRunner;
import com.marketplace.fixtures.LoadAddPicture;
import com.marketplace.fixtures.LoadCreateAdEvent;
import com.marketplace.fixtures.LoadResizePicture;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ResizePictureOfClassifiedAdTest {
    @Test
    void classifiedAdCanBeCreatedAndAPictureCanBeAdded() throws IOException {
        var createClassifiedAd = LoadCreateAdEvent.loadCreateAdDto();

        assertThat(createClassifiedAd).isNotNull();
        assertThat(createClassifiedAd.getOwnerId()).isNotNull();

        var repository = ApplicationRunner.getBean(ClassifiedAdRepository.class);
        var controller = ApplicationRunner.getBean(ClassifiedAdController.class);
        assert controller != null;
        CreateAdResponse ad = controller.createAd(createClassifiedAd);

        assertThat(ad).isNotNull();
        assertThat(ad.getId()).isNotNull();
        assertThat(ad.getOwnerId()).isNotNull().isEqualByComparingTo(createClassifiedAd.getOwnerId());

        AddPictureToClassifiedAd addPictureToClassifiedAd = LoadAddPicture.load();
        addPictureToClassifiedAd.setId(ad.getId());
        AddPictureResponse addPictureResponse = controller.addPicture(addPictureToClassifiedAd);

        ResizeClassifiedAdPicture resizeClassifiedAdPicture = LoadResizePicture.load();
        resizeClassifiedAdPicture.setClassifiedAdId(ad.getId());
        resizeClassifiedAdPicture.setId(addPictureResponse.getId());
        controller.resizePicture(resizeClassifiedAdPicture);

        assert repository != null;
        Optional<ClassifiedAd> load = repository.load(new ClassifiedAdId(ad.getId()));
        assertThat(load).isPresent();

        ClassifiedAd classifiedAd = load.get();
        assertThat(classifiedAd.getChanges()).isNotNull().hasSize(5);
        assertThat(classifiedAd.getPictures()).hasSize(1);
        assertThat(classifiedAd.getPictures().get(0).getSize().width()).isEqualTo(900);
        assertThat(classifiedAd.getPictures().get(0).getSize().height()).isEqualTo(700);
    }
}
