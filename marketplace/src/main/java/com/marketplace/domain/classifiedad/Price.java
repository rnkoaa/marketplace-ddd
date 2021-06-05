package com.marketplace.domain.classifiedad;

import com.marketplace.domain.InvalidStateException;
import java.math.BigDecimal;

public record Price(Money money) {

    public static final Price NEW_PRICE = new Price(Money.fromDecimal(0.00, "USD"));

    public Price {
        if (money != null && money.amount().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidStateException("Price cannot be less than 0");
        }
    }

    public static Price from(BigDecimal amount) {
        return new Price(new Money(amount, "USD"));
    }

    public boolean valid() {
        return money != null && money.amount() != null && money.amount().compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public String toString() {
        return money.toString();
    }
}
