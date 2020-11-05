package com.marketplace.command;

import com.marketplace.domain.Currency;
import com.marketplace.domain.Money;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class UpdateClassifiedAdPrice {
    BigDecimal price;
    Currency currency;
}
