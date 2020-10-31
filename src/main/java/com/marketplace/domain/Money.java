package com.marketplace.domain;

import com.marketplace.CurrencyMismatchException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal amount, String currencyCode, CurrencyLookup currencyLookup) {

    public Money {
        if (currencyLookup == null) {
            currencyLookup = new DefaultCurrencyLookup();
        }
        if (currencyCode == null || currencyCode.isEmpty())
            throw new IllegalArgumentException("currency code must be specified");

        var currency = currencyLookup.findCurrency(currencyCode);
        if (!currency.inUse()) {
            throw new IllegalArgumentException("Currency " + currencyCode + " is not valid");
        }

        BigDecimal roundedAmount = amount.setScale(currency.decimalPlaces(), RoundingMode.CEILING);
        if (!roundedAmount.equals(amount)) {
            throw new IllegalArgumentException(String.format("Amount in %s cannot have more than %d decimals",
                    currencyCode, currency.decimalPlaces()));
        }
    }

    public Money(String amount, String currency) {
        this(new BigDecimal(amount), currency, new DefaultCurrencyLookup());
    }

    public Money(BigDecimal amount, String currency) {
        this(amount, currency, new DefaultCurrencyLookup());
    }

    @Override
    public String toString() {
        return String.format("%s %s", currencyCode, amount);
    }

    public Money subtract(Money subtrahend) {
        if (!currencyCode.equals(subtrahend.currencyCode))
            throw new CurrencyMismatchException("Cannot subtract amounts with different currencies");

        return new Money(amount.subtract(subtrahend.amount), subtrahend.currencyCode);
    }

    public Money add(Money summand) {
        if (!currencyCode.equals(summand.currencyCode))
            throw new CurrencyMismatchException(
                    "Cannot sum amounts with different currencies");

        return new Money(amount.add(summand.amount), summand.currencyCode);
    }

}
