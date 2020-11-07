package com.marketplace.command;

import com.marketplace.domain.CommandHandlerResult;

public interface CommandHandler<T extends Command> {

    CommandHandlerResult handle(T command);
}
