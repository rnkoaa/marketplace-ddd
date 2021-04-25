package com.marketplace.domain.classifiedad.controller;

import com.marketplace.cqrs.command.CommandHandlerResult;
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
    return classifiedAdService.handle(ImmutableUpdateClassifiedAdOwner.builder()
        .ownerId(ownerId)
        .id(classifiedAdId)
        .build());
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> updateClassifiedTitle(UUID classifiedAdId, String title) {
    return classifiedAdService.handle(ImmutableUpdateClassifiedAdTitle.builder()
        .classifiedAdId(classifiedAdId)
        .title(title)
        .build());
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> updateClassifiedText(UUID classifiedAdId, String text) {
    return classifiedAdService.handle(ImmutableUpdateClassifiedAdText.builder()
        .classifiedAdId(classifiedAdId)
        .text(text).build());
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> approveClassifiedAd(UUID classifiedAdId, UUID approvedBy) {
    return classifiedAdService.handle(ImmutableApproveClassifiedAd.builder()
        .classifiedAdId(classifiedAdId)
        .approverId(approvedBy)
        .build());
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> publishClassifiedAd(UUID classifiedAdId) {
    return classifiedAdService.handle(ImmutablePublishClassifiedAd.builder()
        .classifiedAdId(classifiedAdId)
        .build());
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> updateClassifiedAdPrice(
      UUID classifiedAdId,
      BigDecimal amount,
      String currencyCode) {
    return classifiedAdService.handle(ImmutableUpdateClassifiedAdPrice.builder()
        .classifiedAdId(classifiedAdId)
        .amount(amount)
        .currency(currencyCode)
        .build());
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> addPictures(UUID classifiedAdId, List<PictureDto> pictures) {
    return classifiedAdService.handle(ImmutableAddPicturesToClassifiedAd.builder()
        .classifiedAdId(classifiedAdId)
        .addAllPictures(pictures)
        .build());
  }
}
