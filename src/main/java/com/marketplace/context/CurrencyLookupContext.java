package com.marketplace.context;

import com.marketplace.domain.classifiedad.CurrencyLookup;
import com.marketplace.domain.classifiedad.DefaultCurrencyLookup;

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
