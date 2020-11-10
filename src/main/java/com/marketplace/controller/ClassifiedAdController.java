package com.marketplace.controller;

import com.marketplace.domain.ClassifiedAdService;
import com.marketplace.domain.command.CreateClassifiedAd;
import com.marketplace.domain.command.UpdateClassifiedAd;

public class ClassifiedAdController {

    private final ClassifiedAdService classifiedAdService;
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
}
