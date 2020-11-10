package com.marketplace.domain.classifiedad.command;

import com.marketplace.domain.classifiedad.Currency;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class UpdateClassifiedAdPrice {
    BigDecimal price;
    Currency currency;
}
