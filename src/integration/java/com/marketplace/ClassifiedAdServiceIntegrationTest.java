package com.marketplace;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.cqrs.command.CommandHandlerResult;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Disabled
public class ClassifiedAdServiceIntegrationTest extends BaseRepositoryTest {

  @Test
  void classifiedAdCanBeCreatedAndAPictureCanBeAdded() throws IOException {
    CreateClassifiedAd createAdDto = LoadCreateAdEvent.loadCreateAdDto();

    assertThat(createAdDto).isNotNull();
    assertThat(createAdDto.getOwnerId()).isNotNull();

    CommandHandlerResult<CreateAdResponse> createResponse = classifiedAdService.handle(createAdDto);
//
    assertThat(createResponse.getResult()).isPresent();
    assertThat(createResponse.getResult().get().getClassifiedAdId()).isNotNull();
    assertThat(createResponse.getResult().get().getOwnerId()).isNotNull().isEqualByComparingTo(createAdDto.getOwnerId());
//
    AddPictureToClassifiedAd addPictureToClassifiedAd = ImmutableAddPictureToClassifiedAd.copyOf(LoadAddPicture.load())
        .withClassifiedAdId(createResponse.getResult().get().getClassifiedAdId());

    AddPictureResponse addPictureResponse = classifiedAdService.handle(addPictureToClassifiedAd);
    assertThat(addPictureResponse).isNotNull();
    assertThat(addPictureResponse.getClassifiedAdId()).isPresent();
    assertThat(addPictureResponse.getClassifiedAdId().get()).isEqualByComparingTo(createResponse.getResult().get().getClassifiedAdId());
//        controller.addPicture(addPictureToClassifiedAd);
//
//        assert repository != null;
    Optional<ClassifiedAd> found = classifiedAdService.findById(new ClassifiedAdId(createResponse.getResult().get().getClassifiedAdId()));
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

    assertThat(ad.getResult()).isPresent();
    assertThat(ad.getResult().get().getClassifiedAdId()).isNotNull();
    assertThat(ad.getResult().get().getOwnerId()).isNotNull().isEqualByComparingTo(createCommand.getOwnerId());

    Optional<ClassifiedAd> load = classifiedAdService.findById(new ClassifiedAdId(ad.getResult().get().getClassifiedAdId()));
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

    assertThat(ad.getResult()).isPresent();

    UpdateClassifiedAd updateCommand = ImmutableUpdateClassifiedAd.copyOf(LoadUpdateAdEvent.load())
        .withClassifiedAdId(ad.getResult().get().getClassifiedAdId());

    // id for ad is generated dynamically.
    classifiedAdService.handle(updateCommand);

    Optional<ClassifiedAd> found = classifiedAdService.findById(new ClassifiedAdId(ad.getResult().get().getClassifiedAdId()));
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

    assertThat(ad.getResult()).isPresent();
    assertThat(ad.getResult().get().getClassifiedAdId()).isNotNull();
    assertThat(ad.getResult().get().getOwnerId()).isNotNull().isEqualByComparingTo(createCommand.getOwnerId());

    AddPictureToClassifiedAd addPictureToClassifiedAd = ImmutableAddPictureToClassifiedAd.copyOf(LoadAddPicture.load())
        .withClassifiedAdId(ad.getResult().get().getClassifiedAdId());
    AddPictureResponse addPictureResponse = classifiedAdService.handle(addPictureToClassifiedAd);

    assertThat(addPictureResponse.getClassifiedAdId()).isPresent();
    assertThat(addPictureResponse.getId()).isPresent();

    ResizeClassifiedAdPicture resizePictureCommand = ImmutableResizeClassifiedAdPicture.copyOf(LoadResizePicture.load())
        .withClassifiedAdId(ad.getResult().get().getClassifiedAdId())
        .withId(addPictureResponse.getId().get());
    classifiedAdService.handle(resizePictureCommand);

    Optional<ClassifiedAd> load = classifiedAdService.findById(new ClassifiedAdId(ad.getResult().get().getClassifiedAdId()));
    assertThat(load).isPresent();

    ClassifiedAd classifiedAd = load.get();
    assertThat(classifiedAd.getChanges()).isNotNull().hasSize(4);
    assertThat(classifiedAd.getPictures()).hasSize(1);
    assertThat(classifiedAd.getPictures().get(0).getSize().width()).isEqualTo(900);
    assertThat(classifiedAd.getPictures().get(0).getSize().height()).isEqualTo(700);
  }
}
