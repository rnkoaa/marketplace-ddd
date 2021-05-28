package com.marketplace.cqrs.command;

import java.util.Optional;

import org.immutables.value.Value;

@Value.Immutable
public interface CommandHandlerResult<T> {

    Optional<T> getResult();

    boolean isSuccessful();

    Optional<String> getMessage();
}
