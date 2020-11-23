package com.marketplace.domain.classifiedad.command;

import com.marketplace.command.Command;
import com.marketplace.domain.classifiedad.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateClassifiedAdPrice implements Command {
    private UUID id;
    private BigDecimal amount;
    private String currency;
}
