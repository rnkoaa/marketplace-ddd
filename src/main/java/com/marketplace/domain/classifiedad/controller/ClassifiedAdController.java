package com.marketplace.domain.classifiedad.controller;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.CommandHandlerResult;
import com.marketplace.domain.classifiedad.command.*;
import com.marketplace.domain.classifiedad.service.ClassifiedAdService;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

public class ClassifiedAdController {

    private final ClassifiedAdService classifiedAdService;

    @Inject
    public ClassifiedAdController(ClassifiedAdService classifiedAdService) {
        this.classifiedAdService = classifiedAdService;
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
}
