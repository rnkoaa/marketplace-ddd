package com.marketplace.domain.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.marketplace.event.Event;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder
@JsonDeserialize(builder = ClassifiedAdPriceUpdated.ClassifiedAdPriceUpdatedBuilder.class)
public class ClassifiedAdPriceUpdated implements Event {
    UUID id;
    BigDecimal price;

    String currencyCode;

    public ClassifiedAdPriceUpdated(UUID id, BigDecimal price, String currencyCode) {
        this.id = id;
        this.price = price;
        this.currencyCode = currencyCode;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class ClassifiedAdPriceUpdatedBuilder {

    }
}
