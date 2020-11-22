package com.marketplace.domain.classifiedad.command;

import com.marketplace.command.Command;
import lombok.Value;

import java.util.UUID;

@Value
public class PublishClassifiedAd implements Command {
    UUID id;
}
