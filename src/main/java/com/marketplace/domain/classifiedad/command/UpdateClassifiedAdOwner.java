package com.marketplace.domain.classifiedad.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.command.Command;

import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableUpdateClassifiedAdOwner.class)
@JsonSerialize(as = ImmutableUpdateClassifiedAdOwner.class)
public interface UpdateClassifiedAdOwner extends Command {

  UUID getId();

  UUID getOwnerId();
}
