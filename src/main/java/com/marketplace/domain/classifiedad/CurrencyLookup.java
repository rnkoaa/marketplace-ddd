package com.marketplace.domain.classifiedad;


public interface CurrencyLookup {
    default Currency findCurrency(String code) {
        return new Currency("USD", true, 2);
    }
}
