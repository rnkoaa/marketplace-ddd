package com.marketplace.domain.classifiedad.command;

import com.marketplace.command.CommandHandler;
import com.marketplace.controller.CreateAdResponse;
import com.marketplace.domain.classifiedad.*;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdRepository;
import com.marketplace.domain.shared.UserId;
import com.marketplace.framework.Strings;

public class CreateClassifiedAdCommandHandler implements CommandHandler<CreateClassifiedAd> {

    private final ClassifiedAdRepository classifiedAdRepository;

    public CreateClassifiedAdCommandHandler(ClassifiedAdRepository classifiedAdRepository) {
        this.classifiedAdRepository = classifiedAdRepository;
    }

    @Override
    public CommandHandlerResult<CreateAdResponse> handle(CreateClassifiedAd command) {
        var classifiedAdId = new ClassifiedAdId();
        var ownerId = new UserId(command.getUserId());
        var classifiedAd = new ClassifiedAd(classifiedAdId, ownerId);

        if (!Strings.isNullOrEmpty(command.getTitle())) {
            classifiedAd.updateTitle(new ClassifiedAdTitle(command.getTitle()));
        }

        if (!Strings.isNullOrEmpty(command.getText())) {
            classifiedAd.updateText(new ClassifiedAdText(command.getText()));
        }

        classifiedAdRepository.add(classifiedAd);
        var classifiedAdResponse = new CreateAdResponse(command.getUserId(), classifiedAdId.id());

        return new CommandHandlerResult<>(classifiedAdResponse, true, "");
    }
}
