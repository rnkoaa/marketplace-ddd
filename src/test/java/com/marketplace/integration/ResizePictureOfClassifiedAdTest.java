package com.marketplace.integration;

import com.marketplace.controller.*;
import com.marketplace.domain.ClassifiedAd;
import com.marketplace.domain.ClassifiedAdId;
import com.marketplace.domain.ClassifiedAdRepository;
import com.marketplace.fixtures.ApplicationRunner;
import com.marketplace.fixtures.LoadAddPicture;
import com.marketplace.fixtures.LoadCreateAdEvent;
import com.marketplace.fixtures.LoadResizePicture;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ResizePictureOfClassifiedAdTest {
    //
    @Test
    void classifiedAdCanBeCreatedAndAPictureCanBeAdded() throws IOException {
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

        AddPictureDto addPictureDto = LoadAddPicture.load();
        addPictureDto.setClassifiedAdId(ad.getId());
        AddPictureResponse addPictureResponse = controller.addPicture(addPictureDto);

        ResizePictureDto resizePictureDto = LoadResizePicture.load();
        resizePictureDto.setClassifiedAdId(ad.getId());
        resizePictureDto.setId(addPictureResponse.getId());
        controller.resizePicture(resizePictureDto);

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
