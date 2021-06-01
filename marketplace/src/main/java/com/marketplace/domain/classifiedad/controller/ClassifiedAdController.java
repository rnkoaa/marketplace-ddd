package com.marketplace.domain.classifiedad.controller;

import com.marketplace.domain.classifiedad.command.ImmutableApproveClassifiedAd;
import com.marketplace.domain.classifiedad.command.ImmutablePublishClassifiedAd;
import com.marketplace.domain.classifiedad.command.ImmutableUpdateClassifiedAdOwner;
import com.marketplace.domain.classifiedad.command.ImmutableUpdateClassifiedAdPrice;
import com.marketplace.domain.classifiedad.command.ImmutableUpdateClassifiedAdText;
import com.marketplace.domain.classifiedad.command.ImmutableUpdateClassifiedAdTitle;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PictureDto;
import com.marketplace.domain.classifiedad.query.ClassifiedAdQueryEntity;
import com.marketplace.domain.classifiedad.query.ClassifiedAdQueryService;
import com.marketplace.domain.classifiedad.service.ClassifiedAdService;
import io.vavr.control.Try;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

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


  public Try<UpdateClassifiedAdResponse> updateClassifiedAd(UpdateClassifiedAd updateClassifiedAd) {
    return classifiedAdService.handle(updateClassifiedAd);
  }

  public Try<UpdateClassifiedAdResponse> addPicture(AddPictureToClassifiedAd addPictureToClassifiedAd) {
    return classifiedAdService.handle(addPictureToClassifiedAd);
  }

  public Try<UpdateClassifiedAdResponse> resizePicture(ResizeClassifiedAdPicture resizeClassifiedAdPicture) {
    return classifiedAdService.handle(resizeClassifiedAdPicture);
  }


  public Optional<ClassifiedAdQueryEntity> findEntityById(UUID classifiedAdId) {
    return classifiedAdQueryService.findById(classifiedAdId);
  }

  public Try<UpdateClassifiedAdResponse> updateClassifiedAdOwner(UUID classifiedAdId, UUID ownerId) {
    return classifiedAdService.handle(ImmutableUpdateClassifiedAdOwner.builder()
        .ownerId(ownerId)
        .classifiedAdId(classifiedAdId)
        .build());
  }

  public Try<UpdateClassifiedAdResponse> updateClassifiedTitle(UUID classifiedAdId, String title) {
    return classifiedAdService.handle(ImmutableUpdateClassifiedAdTitle.builder()
        .classifiedAdId(classifiedAdId)
        .title(title)
        .build());
  }

  public Try<UpdateClassifiedAdResponse> updateClassifiedText(UUID classifiedAdId, String text) {
    return classifiedAdService.handle(ImmutableUpdateClassifiedAdText.builder()
        .classifiedAdId(classifiedAdId)
        .text(text).build());
  }

  public Try<UpdateClassifiedAdResponse> approveClassifiedAd(UUID classifiedAdId, UUID approvedBy) {
    return classifiedAdService.handle(ImmutableApproveClassifiedAd.builder()
        .classifiedAdId(classifiedAdId)
        .approverId(approvedBy)
        .build());
  }

  public Try<UpdateClassifiedAdResponse> publishClassifiedAd(UUID classifiedAdId) {
    return classifiedAdService.handle(ImmutablePublishClassifiedAd.builder()
        .classifiedAdId(classifiedAdId)
        .build());
  }

  public Try<UpdateClassifiedAdResponse> updateClassifiedAdPrice(
      UUID classifiedAdId,
      BigDecimal amount,
      String currencyCode) {
    return classifiedAdService.handle(ImmutableUpdateClassifiedAdPrice.builder()
        .classifiedAdId(classifiedAdId)
        .amount(amount)
        .currency(currencyCode)
        .build());
  }

  public Try<UpdateClassifiedAdResponse> addPictures(UUID classifiedAdId, List<PictureDto> pictures) {
    return classifiedAdService.handle(ImmutableAddPicturesToClassifiedAd.builder()
        .classifiedAdId(classifiedAdId)
        .addAllPictures(pictures)
        .build());
  }
}
