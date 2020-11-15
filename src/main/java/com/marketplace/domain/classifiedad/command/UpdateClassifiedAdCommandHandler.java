package com.marketplace.domain.classifiedad.command;

import com.marketplace.command.CommandHandler;
import com.marketplace.controller.UpdateClassifiedAdResponse;
import com.marketplace.domain.classifiedad.*;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdRepository;
import com.marketplace.domain.shared.UserId;
import com.marketplace.framework.Strings;

import javax.inject.Inject;
import java.util.Optional;

public class UpdateClassifiedAdCommandHandler implements CommandHandler<UpdateClassifiedAd> {
    private final ClassifiedAdRepository classifiedAdRepository;

    @Inject
    public UpdateClassifiedAdCommandHandler(ClassifiedAdRepository classifiedAdRepository) {
        this.classifiedAdRepository = classifiedAdRepository;
    }

    @Override
    public CommandHandlerResult<UpdateClassifiedAdResponse> handle(UpdateClassifiedAd command) {
        Optional<ClassifiedAd> mayBe = classifiedAdRepository.load(new ClassifiedAdId(command.getId()));
        return mayBe.map(classifiedAd -> {
            if (command.getOwnerId() != null) {
                classifiedAd.updateOwner(new UserId(command.getOwnerId()));
            }
            if (!Strings.isNullOrEmpty(command.getTitle())) {
                classifiedAd.updateTitle(new ClassifiedAdTitle(command.getTitle()));
            }
            if (!Strings.isNullOrEmpty(command.getText())) {
                classifiedAd.updateText(new ClassifiedAdText(command.getText()));
            }
            if (command.getPrice() != null) {
                classifiedAd.updatePrice(command.getPrice());
            }

            if (command.getApprovedBy() != null) {
                classifiedAd.approve(new UserId(command.getApprovedBy()));
            }

            return classifiedAdRepository.add(classifiedAd);
        }).map(classifiedAd -> {
            var updateResponse = new UpdateClassifiedAdResponse();
            return new CommandHandlerResult<>(updateResponse, true, "");
        }).orElse(new CommandHandlerResult<>(null, false, "classifiedAd not found"));
    }
}
