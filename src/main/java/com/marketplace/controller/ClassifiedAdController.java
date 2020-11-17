package com.marketplace.controller;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.service.ClassifiedAdService;
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

    public CreateAdResponse createAd(CreateAdDto createAdDto) {
        CreateClassifiedAd createClassifiedAd = CreateClassifiedAd.from(createAdDto);

        var commandHandlerResult = classifiedAdService.handleCreate(createClassifiedAd);

        return commandHandlerResult.result;
    }

    public UpdateClassifiedAdResponse updateClassifiedAd(UpdateAdDto updateAdDto) {
        UpdateClassifiedAd updateClassifiedAd = UpdateClassifiedAd.from(updateAdDto);

        var commandHandlerResult = classifiedAdService.handleUpdate(updateClassifiedAd);

        return commandHandlerResult.result;
    }

    public AddPictureResponse addPicture(AddPictureDto addPictureDto) {
        return classifiedAdService.handleAddPicture(addPictureDto);
    }

    public ResizePictureResponse resizePicture(ResizePictureDto resizePictureDto) {
        return classifiedAdService.handleResizePicture(resizePictureDto);
    }

    public Optional<ClassifiedAd> findClassifiedAdById(ClassifiedAdId classifiedAdId) {
       return classifiedAdService.findById(classifiedAdId);
    }
}
