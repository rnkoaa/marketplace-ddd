package com.marketplace.domain.command;

import com.marketplace.command.CommandHandler;
import com.marketplace.command.UpdateClassifiedAd;
import com.marketplace.controller.UpdateClassifiedAdResponse;
import com.marketplace.domain.ClassifiedAd;
import com.marketplace.domain.ClassifiedAdId;
import com.marketplace.domain.ClassifiedAdRepository;
import com.marketplace.domain.CommandHandlerResult;

import java.util.Optional;

public class UpdateClassifiedAdCommandHandler implements CommandHandler<UpdateClassifiedAd> {
    private final ClassifiedAdRepository classifiedAdRepository;

    public UpdateClassifiedAdCommandHandler(ClassifiedAdRepository classifiedAdRepository) {
        this.classifiedAdRepository = classifiedAdRepository;
    }

    @Override
    public CommandHandlerResult<UpdateClassifiedAdResponse> handle(UpdateClassifiedAd command) {
        Optional<ClassifiedAd> mayBe = classifiedAdRepository.load(new ClassifiedAdId(command.getId()));
        return mayBe.map(classifiedAd -> {
            // TODO handle all classifiedAd updates here
            return new CommandHandlerResult<UpdateClassifiedAdResponse>(null, true, "");
        }).orElse(new CommandHandlerResult<>(null, false, "classifiedAd not found"));
    }
}
