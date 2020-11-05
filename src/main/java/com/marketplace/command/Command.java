package com.marketplace.command;

import java.time.Instant;

public interface Command {
    Instant createdAt();
}
