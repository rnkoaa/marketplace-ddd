package com.marketplace.domain.classifiedad.command;

import com.marketplace.command.Command;
import com.marketplace.domain.classifiedad.Currency;
import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class UpdateClassifiedAdPrice implements Command {
    private UUID id;
    private BigDecimal price;
    private Currency currency;
}
