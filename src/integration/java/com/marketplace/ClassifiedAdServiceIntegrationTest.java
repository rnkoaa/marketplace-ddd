package com.marketplace;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.CommandHandlerResult;
import com.marketplace.domain.classifiedad.command.CreateClassifiedAd;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd;
import com.marketplace.domain.classifiedad.controller.AddPictureResponse;
import com.marketplace.domain.classifiedad.controller.AddPictureToClassifiedAd;
import com.marketplace.domain.classifiedad.controller.CreateAdResponse;
import com.marketplace.domain.classifiedad.controller.ResizeClassifiedAdPicture;
import com.marketplace.fixtures.LoadAddPicture;
import com.marketplace.fixtures.LoadCreateAdEvent;
import com.marketplace.fixtures.LoadResizePicture;
import com.marketplace.fixtures.LoadUpdateAdEvent;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ClassifiedAdServiceIntegrationTest extends BaseMongoRepositoryTest {

    @Test
    void classifiedAdCanBeCreatedAndAPictureCanBeAdded() throws IOException {
        CreateClassifiedAd createAdDto = LoadCreateAdEvent.loadCreateAdDto();

        assertThat(createAdDto).isNotNull();
        assertThat(createAdDto.getOwnerId()).isNotNull();

        CommandHandlerResult<CreateAdResponse> createResponse = classifiedAdService.handle(createAdDto);
//
        assertThat(createResponse.result).isNotNull();
        assertThat(createResponse.getResult().getId()).isNotNull();
        assertThat(createResponse.getResult().getOwnerId()).isNotNull().isEqualByComparingTo(createAdDto.getOwnerId());
//
        AddPictureToClassifiedAd addPictureToClassifiedAd = LoadAddPicture.load();
        addPictureToClassifiedAd.setId(createResponse.result.getId());

        AddPictureResponse addPictureResponse = classifiedAdService.handle(addPictureToClassifiedAd);
        assertThat(addPictureResponse).isNotNull();
        assertThat(addPictureResponse.getClassifiedAdId()).isEqualByComparingTo(createResponse.result.getId());
//        controller.addPicture(addPictureToClassifiedAd);
//
//        assert repository != null;
        Optional<ClassifiedAd> found = classifiedAdService.findById(new ClassifiedAdId(createResponse.result.getId()));
        assertThat(found).isPresent();
//
        ClassifiedAd classifiedAd = found.get();
        assertThat(classifiedAd.getChanges()).isNotNull().hasSize(4);
        assertThat(classifiedAd.getPictures()).hasSize(1);

    }

    @Test
    void classifiedAdCanBeCreated() throws IOException {
        CreateClassifiedAd createCommand = LoadCreateAdEvent.loadCreateAdDto();

        assertThat(createCommand).isNotNull();
        assertThat(createCommand.getOwnerId()).isNotNull();

        var ad = classifiedAdService.handle(createCommand);

        assertThat(ad.result).isNotNull();
        assertThat(ad.result.getId()).isNotNull();
        assertThat(ad.result.getOwnerId()).isNotNull().isEqualByComparingTo(createCommand.getOwnerId());

        Optional<ClassifiedAd> load = classifiedAdService.findById(new ClassifiedAdId(ad.result.getId()));
        assertThat(load).isPresent();

        ClassifiedAd classifiedAd = load.get();
        assertThat(classifiedAd.getChanges()).isNotNull().hasSize(3);
    }

    @Test
    void classifiedAdCanBeCreatedAndUpdated() throws IOException {
        CreateClassifiedAd createCommand = LoadCreateAdEvent.loadCreateAdDto();

        assertThat(createCommand).isNotNull();
        assertThat(createCommand.getOwnerId()).isNotNull();

        var ad = classifiedAdService.handle(createCommand);

        assertThat(ad.result).isNotNull();

        UpdateClassifiedAd updateCommand = LoadUpdateAdEvent.load();

        // id for ad is generated dynamically.
        updateCommand.setId(ad.result.getId());
        classifiedAdService.handle(updateCommand);

        Optional<ClassifiedAd> found = classifiedAdService.findById(new ClassifiedAdId(ad.result.getId()));
        assertThat(found).isPresent();

        ClassifiedAd classifiedAd = found.get();
        assertThat(classifiedAd.getChanges()).isNotNull().hasSize(3);

        assertThat(classifiedAd.getText().toString()).startsWith("update");
        assertThat(classifiedAd.getTitle().toString()).startsWith("update");
    }

    @Test
    void classifiedAdCanBeCreatedAndAPictureCanBeAddedAndResized() throws IOException {
        var createCommand = LoadCreateAdEvent.loadCreateAdDto();

        assertThat(createCommand).isNotNull();
        assertThat(createCommand.getOwnerId()).isNotNull();

        var ad = classifiedAdService.handle(createCommand);

        assertThat(ad.result).isNotNull();
        assertThat(ad.result.getId()).isNotNull();
        assertThat(ad.result.getOwnerId()).isNotNull().isEqualByComparingTo(createCommand.getOwnerId());

        AddPictureToClassifiedAd addPictureToClassifiedAd = LoadAddPicture.load();
        addPictureToClassifiedAd.setId(ad.result.getId());
        AddPictureResponse addPictureResponse = classifiedAdService.handle(addPictureToClassifiedAd);

        ResizeClassifiedAdPicture resizePictureCommand = LoadResizePicture.load();
        resizePictureCommand.setClassifiedAdId(ad.result.getId());
        resizePictureCommand.setId(addPictureResponse.getId());
        classifiedAdService.handle(resizePictureCommand);

        Optional<ClassifiedAd> load = classifiedAdService.findById(new ClassifiedAdId(ad.result.getId()));
        assertThat(load).isPresent();

        ClassifiedAd classifiedAd = load.get();
        assertThat(classifiedAd.getChanges()).isNotNull().hasSize(4);
        assertThat(classifiedAd.getPictures()).hasSize(1);
        assertThat(classifiedAd.getPictures().get(0).getSize().width()).isEqualTo(900);
        assertThat(classifiedAd.getPictures().get(0).getSize().height()).isEqualTo(700);
    }
}
