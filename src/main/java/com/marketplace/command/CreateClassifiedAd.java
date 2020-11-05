package com.marketplace.command;

import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class CreateClassifiedAd implements Command {
    UUID userId;
    Instant createdAt;
    String title;
    String text;

    @Override
    public Instant createdAt() {
        return createdAt;
    }
}
