package com.marketplace.domain.classifiedad.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.cqrs.event.VersionedEvent;
import java.math.BigDecimal;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonSerialize(as = ImmutableClassifiedAdPriceUpdated.class)
@JsonDeserialize(as = ImmutableClassifiedAdPriceUpdated.class)
public interface ClassifiedAdPriceUpdated extends VersionedEvent {

  UUID getId();

  BigDecimal getPrice();

  String getCurrency();

}
