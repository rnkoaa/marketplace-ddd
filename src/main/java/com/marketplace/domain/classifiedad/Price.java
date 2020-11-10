package com.marketplace.domain.classifiedad;

public record Price(Money money) {
    public Price {
        if (money.amount().doubleValue() < 0) {
            throw new IllegalArgumentException("Price cannot be less than 0");
        }
    }

    @Override
    public String toString() {
        return money.toString();
    }
}
