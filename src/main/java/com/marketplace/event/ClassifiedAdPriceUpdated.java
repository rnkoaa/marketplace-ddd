package com.marketplace.event;

import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
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

}
