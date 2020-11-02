package com.marketplace.domain;

import com.marketplace.CurrencyMismatchException;
import com.marketplace.fixtures.FakeCurrencyLookup;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MoneyTest {

    private final CurrencyLookup currencyLookup = new FakeCurrencyLookup();

    @Test
    void twoOfTheSameAmountShouldBeEqual() {
        var firstAmount = Money.fromDecimal(5.00, "EUR", currencyLookup);
        var secondAmount = Money.fromDecimal(5.00, "EUR", currencyLookup);
        assertThat(firstAmount).isEqualTo(secondAmount);
    }

    @Test
    void twoOfTheSameAmountButDifferentCurrenciesShouldNotBeEqual() {
        var firstAmount = Money.fromDecimal(5.00, "EUR", currencyLookup);
        var secondAmount = Money.fromDecimal(5.00, "USD", currencyLookup);
        assertThat(firstAmount)
                .isNotEqualTo(secondAmount);
    }

    @Test
    public void fromString_and_FromDecimal_should_be_equal() {
        var firstAmount = Money.fromDecimal(5, "EUR", currencyLookup);
        var secondAmount = Money.fromString("5.00", "EUR", currencyLookup);

        assertThat(firstAmount).isEqualTo(secondAmount);
    }

    @Test
    public void sum_of_money_gives_full_amount() {
        var coin1 = Money.fromDecimal(1, "EUR", currencyLookup);
        var coin2 = Money.fromDecimal(2, "EUR", currencyLookup);
        var coin3 = Money.fromDecimal(2, "EUR", currencyLookup);

        var banknote = Money.fromDecimal(5, "EUR", currencyLookup);

        var total = coin1.add(coin2).add(coin3);
        assertThat(banknote).isEqualTo(total);
    }

    @Test
    public void unused_currency_should_not_be_allowed() {
        assertThatThrownBy(() -> {
            Money.fromDecimal(100, "DEM", currencyLookup);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Currency DEM is not valid");
    }

    @Test
    public void unknown_currency_should_not_be_allowed() {
        assertThatThrownBy(() -> {
            Money.fromDecimal(100, "WHAT?", currencyLookup);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Currency WHAT? is not valid");
    }

    @Test
    public void throw_when_too_many_decimal_places() {
        assertThatThrownBy(() -> {
            Money.fromString("100.1234", "USD", currencyLookup);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount in USD cannot have more than 2 decimals");
    }

    @Test
    public void throws_on_adding_different_currencies() {
        var firstAmount = Money.fromDecimal(5, "USD", currencyLookup);
        var secondAmount = Money.fromDecimal(5, "EUR", currencyLookup);

        assertThatThrownBy(() -> {
            firstAmount.add(secondAmount);
        }).isInstanceOf(CurrencyMismatchException.class)
                .hasMessage("Cannot sum amounts with different currencies");
    }

    @Test
    public void throws_on_substracting_different_currencies() {
        var firstAmount = Money.fromDecimal(5, "USD", currencyLookup);
        var secondAmount = Money.fromDecimal(5, "EUR", currencyLookup);

        assertThatThrownBy(() -> {
            firstAmount.subtract(secondAmount);
        }).isInstanceOf(CurrencyMismatchException.class)
                .hasMessage("Cannot subtract amounts with different currencies");
    }
}