package com.marketplace.cqrs.command;


public interface CommandHandler<T extends Command> {

    CommandHandlerResult handle(T command);
}
