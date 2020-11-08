package com.marketplace.domain.command;

import com.marketplace.command.Command;
import com.marketplace.controller.CreateAdDto;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class CreateClassifiedAd implements Command {
    UUID userId;
    Instant createdAt;
    String title;
    String text;

    public static CreateClassifiedAd from(CreateAdDto createAdDto) {
        return new CreateClassifiedAd(createAdDto.getOwnerId(), Instant.now(),
                createAdDto.getTitle(), createAdDto.getText());
    }

    @Override
    public Instant createdAt() {
        return createdAt;
    }
}
