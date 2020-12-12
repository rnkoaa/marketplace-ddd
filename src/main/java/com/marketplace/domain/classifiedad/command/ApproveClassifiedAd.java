package com.marketplace.domain.classifiedad.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.command.Command;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableApproveClassifiedAd.class)
@JsonDeserialize(as = ImmutableApproveClassifiedAd.class)
public interface ApproveClassifiedAd extends Command {
     UUID getClassifiedAdId();
     UUID getApproverId();
}
