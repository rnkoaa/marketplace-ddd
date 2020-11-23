package com.marketplace.domain.classifiedad.controller;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.CommandHandlerResult;
import com.marketplace.domain.classifiedad.command.ApproveClassifiedAd;
import com.marketplace.domain.classifiedad.command.CreateClassifiedAd;
import com.marketplace.domain.classifiedad.command.PublishClassifiedAd;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PictureDto;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAdOwner;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAdPrice;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAdText;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAdTitle;
import com.marketplace.domain.classifiedad.read.ClassifiedAdReadEntity;
import com.marketplace.domain.classifiedad.read.ClassifiedAdReadService;
import com.marketplace.domain.classifiedad.service.ClassifiedAdService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;

public class ClassifiedAdController {

  private final ClassifiedAdService classifiedAdService;
  private final ClassifiedAdReadService classifiedAdReadService;

  @Inject
  public ClassifiedAdController(ClassifiedAdService classifiedAdService,
      ClassifiedAdReadService classifiedAdReadService) {
    this.classifiedAdService = classifiedAdService;
    this.classifiedAdReadService = classifiedAdReadService;
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

  public Optional<ClassifiedAd> findClassifiedAdById(ClassifiedAdId classifiedAdId) {
    return classifiedAdService.findById(classifiedAdId);
  }

  public Optional<ClassifiedAdReadEntity> findEntityById(UUID classifiedAdId) {
    return classifiedAdReadService.findById(classifiedAdId);
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
