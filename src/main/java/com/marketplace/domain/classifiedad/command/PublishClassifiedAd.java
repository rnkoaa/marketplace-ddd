package com.marketplace.domain.classifiedad.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.command.Command;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutablePublishClassifiedAd.class)
@JsonDeserialize(as = ImmutablePublishClassifiedAd.class)
public interface PublishClassifiedAd extends Command {
     UUID getClassifiedAdId();
}
