package com.marketplace.domain.classifiedad.controller;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.CommandHandlerResult;
import com.marketplace.domain.userprofile.service.ClassifiedAdService;
import com.marketplace.domain.classifiedad.command.CreateClassifiedAd;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd;

import javax.inject.Inject;
import java.util.Optional;

public class ClassifiedAdController {

    private final ClassifiedAdService classifiedAdService;

    @Inject
    public ClassifiedAdController(ClassifiedAdService classifiedAdService) {
        this.classifiedAdService = classifiedAdService;
    }

    public CommandHandlerResult<CreateAdResponse> createAd(CreateClassifiedAd createClassifiedAd) {
        return classifiedAdService.handleCreate(createClassifiedAd);
    }

    public CommandHandlerResult<UpdateClassifiedAdResponse> updateClassifiedAd(UpdateClassifiedAd updateClassifiedAd) {
        return classifiedAdService.handleUpdate(updateClassifiedAd);
    }

    public AddPictureResponse addPicture(AddPictureToClassifiedAd addPictureToClassifiedAd) {
        return classifiedAdService.handleAddPicture(addPictureToClassifiedAd);
    }

    public ResizePictureResponse resizePicture(ResizeClassifiedAdPicture resizeClassifiedAdPicture) {
        return classifiedAdService.handleResizePicture(resizeClassifiedAdPicture);
    }

    public Optional<ClassifiedAd> findClassifiedAdById(ClassifiedAdId classifiedAdId) {
        return classifiedAdService.findById(classifiedAdId);
    }
}
