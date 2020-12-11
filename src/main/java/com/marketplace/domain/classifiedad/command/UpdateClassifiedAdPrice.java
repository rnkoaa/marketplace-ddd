package com.marketplace.domain.classifiedad.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.command.Command;

import java.math.BigDecimal;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonSerialize(as = ImmutableUpdateClassifiedAdPrice.class)
@JsonDeserialize(as = ImmutableUpdateClassifiedAdPrice.class)
public interface UpdateClassifiedAdPrice extends Command {

  UUID getClassifiedAdId();

  BigDecimal getAmount();

  String getCurrency();
}
