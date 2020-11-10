package com.marketplace.fixtures;

import com.marketplace.domain.classifiedad.Currency;
import com.marketplace.domain.classifiedad.CurrencyLookup;

import java.util.List;

public class FakeCurrencyLookup implements CurrencyLookup {
    private final List<Currency> currencies = List.of(
            new Currency("EUR", true, 2),
            new Currency("USD", true, 2),
            new Currency("JPY", true, 2),
            new Currency("DEM", false, 2)
    );

    @Override
    public Currency findCurrency(String code) {
        return currencies.stream()
                .filter(it -> it.code().equals(code))
                .findFirst()
                .orElse(Currency.none());
    }
}
