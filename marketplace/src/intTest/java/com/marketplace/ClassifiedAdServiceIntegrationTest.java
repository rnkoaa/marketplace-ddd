package com.marketplace;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.command.CreateClassifiedAd;
import com.marketplace.domain.classifiedad.command.ImmutableUpdateClassifiedAd;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd;
import com.marketplace.domain.classifiedad.controller.AddPictureResponse;
import com.marketplace.domain.classifiedad.controller.AddPictureToClassifiedAd;
import com.marketplace.domain.classifiedad.controller.CreateAdResponse;
import com.marketplace.domain.classifiedad.controller.ImmutableAddPictureToClassifiedAd;
import com.marketplace.domain.classifiedad.controller.ImmutableResizeClassifiedAdPicture;
import com.marketplace.domain.classifiedad.controller.ResizeClassifiedAdPicture;
import com.marketplace.fixtures.LoadAddPicture;
import com.marketplace.fixtures.LoadCreateAdEvent;
import com.marketplace.fixtures.LoadResizePicture;
import com.marketplace.fixtures.LoadUpdateAdEvent;
import io.vavr.control.Try;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class ClassifiedAdServiceIntegrationTest extends BaseRepositoryTest {

    @Test
    void classifiedAdCanBeCreatedAndAPictureCanBeAdded() throws IOException {
        CreateClassifiedAd createAdDto = LoadCreateAdEvent.loadCreateAdDto();

        assertThat(createAdDto).isNotNull();
        assertThat(createAdDto.getOwnerId()).isNotNull();

        Try<CreateAdResponse> createResponse = classifiedAdService.handle(createAdDto);
        assertThat(createResponse.isSuccess()).isTrue();

        CreateAdResponse adResponse = createResponse.get();
//
        assertThat(adResponse.getClassifiedAdId()).isNotNull();
        assertThat(adResponse.getOwnerId()).isNotNull().isEqualByComparingTo(createAdDto.getOwnerId());
//
        AddPictureToClassifiedAd addPictureToClassifiedAd = ImmutableAddPictureToClassifiedAd
            .copyOf(LoadAddPicture.load())
            .withClassifiedAdId(adResponse.getClassifiedAdId());

        AddPictureResponse addPictureResponse = classifiedAdService.handle(addPictureToClassifiedAd);
        assertThat(addPictureResponse).isNotNull();
        assertThat(addPictureResponse.getClassifiedAdId()).isPresent();
        assertThat(addPictureResponse.getClassifiedAdId().get()).isEqualByComparingTo(adResponse.getClassifiedAdId());
//        controller.addPicture(addPictureToClassifiedAd);
//
//        assert repository != null;
        Optional<ClassifiedAd> found = classifiedAdService.findById(new ClassifiedAdId(adResponse.getClassifiedAdId()));
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

        var mayBeAd = classifiedAdService.handle(createCommand);
        assertThat(mayBeAd.isSuccess()).isTrue();

        CreateAdResponse adResponse = mayBeAd.get();

//    assertThat(ad.getResult()).isPresent();
        assertThat(adResponse.getClassifiedAdId()).isNotNull();
        assertThat(adResponse.getOwnerId()).isNotNull().isEqualByComparingTo(createCommand.getOwnerId());

        Optional<ClassifiedAd> load = classifiedAdService.findById(new ClassifiedAdId(adResponse.getClassifiedAdId()));
        assertThat(load).isPresent();

        ClassifiedAd classifiedAd = load.get();
        assertThat(classifiedAd.getChanges()).isNotNull().hasSize(3);
    }

    @Test
    void classifiedAdCanBeCreatedAndUpdated() throws IOException {
        CreateClassifiedAd createCommand = LoadCreateAdEvent.loadCreateAdDto();

        assertThat(createCommand).isNotNull();
        assertThat(createCommand.getOwnerId()).isNotNull();

        var mayBeAd = classifiedAdService.handle(createCommand);
        assertThat(mayBeAd.isSuccess()).isTrue();
        CreateAdResponse adResponse = mayBeAd.get();

//    assertThat(ad.getResult()).isPresent();

        UpdateClassifiedAd updateCommand = ImmutableUpdateClassifiedAd.copyOf(LoadUpdateAdEvent.load())
            .withClassifiedAdId(adResponse.getClassifiedAdId());

        // id for ad is generated dynamically.
        classifiedAdService.handle(updateCommand);

        Optional<ClassifiedAd> found = classifiedAdService.findById(new ClassifiedAdId(adResponse.getClassifiedAdId()));
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

        var mayBeAd = classifiedAdService.handle(createCommand);
        assertThat(mayBeAd.isSuccess()).isTrue();

        CreateAdResponse adResponse = mayBeAd.get();

        assertThat(adResponse.getClassifiedAdId()).isNotNull();
        assertThat(adResponse.getOwnerId()).isNotNull().isEqualByComparingTo(createCommand.getOwnerId());

        AddPictureToClassifiedAd addPictureToClassifiedAd = ImmutableAddPictureToClassifiedAd
            .copyOf(LoadAddPicture.load())
            .withClassifiedAdId(adResponse.getClassifiedAdId());
        AddPictureResponse addPictureResponse = classifiedAdService.handle(addPictureToClassifiedAd);

        assertThat(addPictureResponse.getClassifiedAdId()).isPresent();
        assertThat(addPictureResponse.getId()).isPresent();

        ResizeClassifiedAdPicture resizePictureCommand = ImmutableResizeClassifiedAdPicture
            .copyOf(LoadResizePicture.load())
            .withClassifiedAdId(adResponse.getClassifiedAdId())
            .withId(addPictureResponse.getId().get());
        classifiedAdService.handle(resizePictureCommand);

        Optional<ClassifiedAd> load = classifiedAdService.findById(new ClassifiedAdId(adResponse.getClassifiedAdId()));
        assertThat(load).isPresent();

        ClassifiedAd classifiedAd = load.get();
        assertThat(classifiedAd.getChanges()).isNotNull().hasSize(4);
        assertThat(classifiedAd.getPictures()).hasSize(1);
        assertThat(classifiedAd.getPictures().get(0).getSize().width()).isEqualTo(900);
        assertThat(classifiedAd.getPictures().get(0).getSize().height()).isEqualTo(700);
    }
}
