package com.marketplace.domain.classifiedad.controller;

import com.marketplace.domain.classifiedad.CommandHandlerResult;
import com.marketplace.domain.classifiedad.command.*;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PictureDto;
import com.marketplace.domain.classifiedad.query.ClassifiedAdQueryEntity;
import com.marketplace.domain.classifiedad.query.ClassifiedAdQueryService;
import com.marketplace.domain.classifiedad.service.ClassifiedAdService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Named
@Singleton
public class ClassifiedAdController {

  private final ClassifiedAdService classifiedAdService;
  private final ClassifiedAdQueryService classifiedAdQueryService;

  @Inject
  public ClassifiedAdController(ClassifiedAdService classifiedAdService,
      ClassifiedAdQueryService classifiedAdQueryService) {
    this.classifiedAdService = classifiedAdService;
    this.classifiedAdQueryService = classifiedAdQueryService;
  }

  public CommandHandlerResult<CreateAdResponse> createAd(CreateClassifiedAd createClassifiedAd) {
    return classifiedAdService.handle(createClassifiedAd);
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> updateClassifiedAd(UpdateClassifiedAd updateClassifiedAd) {
    return classifiedAdService.handle(updateClassifiedAd);
  }

  public AddPictureResponse addPicture(AddPictureToClassifiedAd addPictureToClassifiedAd) {
    return classifiedAdService.handle(addPictureToClassifiedAd);
  }

  public ResizePictureResponse resizePicture(ResizeClassifiedAdPicture resizeClassifiedAdPicture) {
    return classifiedAdService.handle(resizeClassifiedAdPicture);
  }


  public Optional<ClassifiedAdQueryEntity> findEntityById(UUID classifiedAdId) {
    return classifiedAdQueryService.findById(classifiedAdId);
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> updateClassifiedAdOwner(UUID classifiedAdId, UUID ownerId) {
    return classifiedAdService.handle(new UpdateClassifiedAdOwner(classifiedAdId, ownerId));
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> updateClassifiedTitle(UUID classifiedAdId, String title) {
    return classifiedAdService.handle(new UpdateClassifiedAdTitle(classifiedAdId, title));
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> updateClassifiedText(UUID classifiedAdId, String text) {
    return classifiedAdService.handle(new UpdateClassifiedAdText(classifiedAdId, text));
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> approveClassifiedAd(UUID classifiedAdId, UUID approvedBy) {
    return classifiedAdService.handle(new ApproveClassifiedAd(classifiedAdId, approvedBy));
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> publishClassifiedAd(UUID classifiedAdId) {
    return classifiedAdService.handle(new PublishClassifiedAd(classifiedAdId));
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> updateClassifiedAdPrice(
      UUID classifiedAdId,
      BigDecimal amount,
      String currencyCode) {
    return classifiedAdService.handle(new UpdateClassifiedAdPrice(classifiedAdId, amount, currencyCode));
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> addPictures(UUID classifiedAdId, List<PictureDto> pictures) {
    var command = new
        AddPicturesToClassifiedAd(classifiedAdId, pictures);
    return classifiedAdService.handle(command);
  }
}
