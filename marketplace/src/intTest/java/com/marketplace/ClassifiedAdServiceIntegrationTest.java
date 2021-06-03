package com.marketplace;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.marketplace.domain.PictureId;
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
import com.marketplace.domain.classifiedad.controller.UpdateClassifiedAdResponse;
import com.marketplace.fixtures.LoadAddPicture;
import com.marketplace.fixtures.LoadCreateAdEvent;
import com.marketplace.fixtures.LoadResizePicture;
import com.marketplace.fixtures.LoadUpdateAdEvent;
import io.vavr.control.Try;
import java.io.IOException;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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

        Try<UpdateClassifiedAdResponse> maybeAddPicture = classifiedAdService.handle(addPictureToClassifiedAd);
        assertThat(maybeAddPicture.isSuccess()).isTrue();

        UpdateClassifiedAdResponse addPictureResponse = maybeAddPicture.get();

        assertThat(addPictureResponse.getId()).isNotNull();
        assertThat(addPictureResponse.getId()).isEqualByComparingTo(adResponse.getClassifiedAdId());
//        assert repository != null;
        Optional<ClassifiedAd> found = classifiedAdService.findById(new ClassifiedAdId(adResponse.getClassifiedAdId()));
        assertThat(found).isPresent();
//
        ClassifiedAd classifiedAd = found.get();
        assertThat(createAdDto.getText()).isPresent();
        assertThat(createAdDto.getTitle()).isPresent();
        assertThat(classifiedAd.getTitle().toString()).isEqualTo(createAdDto.getTitle().get());
        assertThat(classifiedAd.getText().toString()).isEqualTo(createAdDto.getText().get());
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

        assertThat(adResponse.getClassifiedAdId()).isNotNull();
        assertThat(adResponse.getOwnerId()).isNotNull().isEqualByComparingTo(createCommand.getOwnerId());

        Optional<ClassifiedAd> load = classifiedAdService.findById(new ClassifiedAdId(adResponse.getClassifiedAdId()));
        assertThat(load).isPresent();

        ClassifiedAd classifiedAd = load.get();
        assertThat(createCommand.getText()).isPresent();
        assertThat(createCommand.getTitle()).isPresent();
        assertThat(classifiedAd.getTitle().toString()).isEqualTo(createCommand.getTitle().get());
        assertThat(classifiedAd.getText().toString()).isEqualTo(createCommand.getText().get());
    }

    @Test
//    @Disabled
    void classifiedAdCanBeCreatedAndUpdated() throws IOException {
        CreateClassifiedAd createCommand = LoadCreateAdEvent.loadCreateAdDto();

        assertThat(createCommand).isNotNull();
        assertThat(createCommand.getOwnerId()).isNotNull();

        var mayBeAd = classifiedAdService.handle(createCommand);
        assertThat(mayBeAd.isSuccess()).isTrue();
        CreateAdResponse adResponse = mayBeAd.get();

        UpdateClassifiedAd updateCommand = ImmutableUpdateClassifiedAd.copyOf(LoadUpdateAdEvent.load())
            .withClassifiedAdId(adResponse.getClassifiedAdId());

        // id for ad is generated dynamically.
        classifiedAdService.handle(updateCommand);

        Optional<ClassifiedAd> found = classifiedAdService.findById(new ClassifiedAdId(adResponse.getClassifiedAdId()));
        assertThat(found).isPresent();

        ClassifiedAd classifiedAd = found.get();
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
        Try<UpdateClassifiedAdResponse> maybeAddResponse = classifiedAdService.handle(addPictureToClassifiedAd);
        assertThat(maybeAddResponse.isSuccess()).isTrue();

        Optional<ClassifiedAd> maybeClassifiedAd = classifiedAdService
            .findById(ClassifiedAdId.from(adResponse.getClassifiedAdId()));
        assertThat(maybeClassifiedAd).isPresent();

        ClassifiedAd classifiedAd = maybeClassifiedAd.get();
        assertThat(classifiedAd.getPictures()).hasSize(1);
        assertThat(classifiedAd.getPictures().get(0).getSize().width()).isEqualTo(800);
        assertThat(classifiedAd.getPictures().get(0).getSize().height()).isEqualTo(600);

        UpdateClassifiedAdResponse addPictureResponse = maybeAddResponse.get();

        assertThat(addPictureResponse.getId()).isNotNull();

        PictureId pictureId = classifiedAd.getPictures().get(0).getId();

        ResizeClassifiedAdPicture resizePictureCommand = ImmutableResizeClassifiedAdPicture
            .copyOf(LoadResizePicture.load())
            .withId(pictureId.id())
            .withClassifiedAdId(adResponse.getClassifiedAdId());
        classifiedAdService.handle(resizePictureCommand);

        Optional<ClassifiedAd> load = classifiedAdService.findById(new ClassifiedAdId(adResponse.getClassifiedAdId()));
        assertThat(load).isPresent();

        ClassifiedAd classifiedAd2 = load.get();
        assertThat(classifiedAd2.getPictures()).hasSize(1);
        assertThat(classifiedAd2.getPictures().get(0).getSize().width()).isEqualTo(900);
        assertThat(classifiedAd2.getPictures().get(0).getSize().height()).isEqualTo(700);
    }
}
