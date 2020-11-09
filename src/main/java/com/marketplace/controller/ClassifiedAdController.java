package com.marketplace.controller;

import com.marketplace.domain.command.CreateClassifiedAd;
import com.marketplace.domain.command.UpdateClassifiedAd;
import com.marketplace.domain.command.CreateClassifiedAdCommandHandler;
import com.marketplace.domain.command.UpdateClassifiedAdCommandHandler;

public class ClassifiedAdController {
    private final CreateClassifiedAdCommandHandler createClassifiedAdCommandHandler;
    private final UpdateClassifiedAdCommandHandler updateClassifiedAdCommandHandler;
    private final ClassifiedAdCommandHandler classifiedAdCommandHandler;

    public ClassifiedAdController(CreateClassifiedAdCommandHandler createClassifiedAdCommandHandler,
                                  UpdateClassifiedAdCommandHandler updateClassifiedAdCommandHandler,
                                  ClassifiedAdCommandHandler classifiedAdCommandHandler) {
        this.createClassifiedAdCommandHandler = createClassifiedAdCommandHandler;
        this.updateClassifiedAdCommandHandler = updateClassifiedAdCommandHandler;
        this.classifiedAdCommandHandler = classifiedAdCommandHandler;
    }

    public CreateAdResponse createAd(CreateAdDto createAdDto) {
        CreateClassifiedAd createClassifiedAd = CreateClassifiedAd.from(createAdDto);

        var commandHandlerResult = createClassifiedAdCommandHandler.handle(createClassifiedAd);

        return commandHandlerResult.result;
    }

    public UpdateClassifiedAdResponse updateClassifiedAd(UpdateAdDto updateAdDto) {
        UpdateClassifiedAd updateClassifiedAd = UpdateClassifiedAd.from(updateAdDto);

        var commandHandlerResult = updateClassifiedAdCommandHandler.handle(updateClassifiedAd);

        return commandHandlerResult.result;
    }

    public AddPictureResponse addPicture(AddPictureDto addPictureDto) {
        return classifiedAdCommandHandler.handleAddPicture(addPictureDto);
    }

    public ResizePictureResponse resizePicture(ResizePictureDto resizePictureDto) {
        return classifiedAdCommandHandler.handleResizePicture(resizePictureDto);
    }
}
