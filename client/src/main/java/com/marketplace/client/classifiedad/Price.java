package com.marketplace.client.classifiedad;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.math.BigDecimal;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutablePrice.class)
public interface Price {

    BigDecimal getAmount();

    String getCurrencyCode();

}
