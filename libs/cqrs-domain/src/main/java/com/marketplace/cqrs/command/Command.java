package com.marketplace.cqrs.command;

import java.time.Instant;
import org.immutables.value.Value;

public interface Command {
    @Value.Default
    default Instant createdAt(){
        return Instant.now();
    }
}
