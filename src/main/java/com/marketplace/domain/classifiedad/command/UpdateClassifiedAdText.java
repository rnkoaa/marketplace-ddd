package com.marketplace.domain.classifiedad.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.command.Command;

import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableUpdateClassifiedAdText.class)
@JsonSerialize(as = ImmutableUpdateClassifiedAdText.class)
public interface UpdateClassifiedAdText extends Command {

  UUID getClassifiedAdId();

  String getText();

}
