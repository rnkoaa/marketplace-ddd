package com.marketplace.domain.classifiedad.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.command.Command;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonSerialize(as = ImmutableUpdateClassifiedAdTitle.class)
@JsonDeserialize(as = ImmutableUpdateClassifiedAdTitle.class)
public interface UpdateClassifiedAdTitle extends Command {

  UUID getClassifiedAdId();

  String getTitle();
}
