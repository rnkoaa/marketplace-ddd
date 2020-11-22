package com.marketplace.domain.classifiedad.controller;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
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

    public CreateAdResponse createAd(CreateClassifiedAd createClassifiedAd) {

        var commandHandlerResult = classifiedAdService.handleCreate(createClassifiedAd);

        return commandHandlerResult.result;
    }

    public UpdateClassifiedAdResponse updateClassifiedAd(UpdateClassifiedAd updateClassifiedAd) {

        var commandHandlerResult = classifiedAdService.handleUpdate(updateClassifiedAd);

        return commandHandlerResult.result;
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
