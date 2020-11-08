package com.marketplace.domain.command;

import com.marketplace.domain.Currency;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class UpdateClassifiedAdPrice {
    BigDecimal price;
    Currency currency;
}
