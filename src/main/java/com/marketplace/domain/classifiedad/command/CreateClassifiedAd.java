package com.marketplace.domain.classifiedad.command;

import com.marketplace.command.Command;
import com.marketplace.domain.classifiedad.ClassifiedAdState;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateClassifiedAd implements Command {
    private UUID id;
    private String text;
    private String title;
    private PriceUpdate price;
    private UUID ownerId;
    private UUID approvedBy;
    private ClassifiedAdState state;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceUpdate {
        private String currencyCode;
        private BigDecimal bigDecimal;
    }
}
