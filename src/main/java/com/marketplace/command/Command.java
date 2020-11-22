package com.marketplace.command;

import java.time.Instant;

public interface Command {
    default Instant createdAt(){
        return Instant.now();
    }
}
