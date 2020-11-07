package com.marketplace.context;

import com.marketplace.domain.CurrencyLookup;
import com.marketplace.domain.DefaultCurrencyLookup;

public class CurrencyLookupContext {
    static CurrencyLookup currencyLookup;

    private CurrencyLookupContext() {
    }

    public static CurrencyLookup defaultCurrencyLookup() {
        if (currencyLookup == null) {
            currencyLookup = new DefaultCurrencyLookup();
        }
        return currencyLookup;
    }
}
