package com.marketplace.controller.classifiedad;

import com.marketplace.domain.classifiedad.ClassifiedAdState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class UpdateAdDto {
    private UUID id;
    private String text;
    private String title;
    private PriceDto price;
    private UUID ownerId;
    private UUID approvedBy;
    private ClassifiedAdState state;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceDto {
        private String currencyCode;
        private BigDecimal bigDecimal;
    }
}


