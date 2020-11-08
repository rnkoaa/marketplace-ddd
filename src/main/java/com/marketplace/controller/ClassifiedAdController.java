package com.marketplace.controller;

import com.marketplace.domain.command.CreateClassifiedAd;
import com.marketplace.domain.command.UpdateClassifiedAd;
import com.marketplace.domain.command.CreateClassifiedAdCommandHandler;
import com.marketplace.domain.command.UpdateClassifiedAdCommandHandler;

public class ClassifiedAdController {
    private final CreateClassifiedAdCommandHandler createClassifiedAdCommandHandler;
    private final UpdateClassifiedAdCommandHandler updateClassifiedAdCommandHandler;

    public ClassifiedAdController(CreateClassifiedAdCommandHandler createClassifiedAdCommandHandler, UpdateClassifiedAdCommandHandler updateClassifiedAdCommandHandler) {
        this.createClassifiedAdCommandHandler = createClassifiedAdCommandHandler;
        this.updateClassifiedAdCommandHandler = updateClassifiedAdCommandHandler;
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
}
