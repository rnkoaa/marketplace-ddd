package com.marketplace.framework.events;

import java.math.BigDecimal;
import java.util.UUID;

public final class ClassifiedAdPriceUpdated implements Event {
    private final UUID id;
    private final BigDecimal price;

    private final String currencyCode;

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
