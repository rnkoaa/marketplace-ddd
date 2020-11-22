package com.marketplace.domain.classifiedad.command;

import com.marketplace.command.Command;
import com.marketplace.domain.classifiedad.ClassifiedAdState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClassifiedAd implements Command {
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

//    private UUID id;
//    private String text;
//    private String title;
//    private Price price;
//    private UUID ownerId;
//    private UUID approvedBy;
//    private ClassifiedAdState state;

//    public static UpdateClassifiedAd from(UpdateAdDto updateAdDto) {
//        UpdateAdDto.PriceDto priceDto = updateAdDto.getPrice();
//        if (priceDto != null) {
//            var money = new Money(priceDto.getBigDecimal(), priceDto.getCurrencyCode(), new DefaultCurrencyLookup());
//            Price price = new Price(money);
//            new UpdateClassifiedAd(updateAdDto.getId(), updateAdDto.getText(), updateAdDto.getTitle(), price,
//                    updateAdDto.getOwnerId(),
//                    updateAdDto.getApprovedBy(),
//                    updateAdDto.getState()
//            );
//        }
//
//        return new UpdateClassifiedAd(updateAdDto.getId(), updateAdDto.getText(), updateAdDto.getTitle(), null,
//                updateAdDto.getOwnerId(),
//                updateAdDto.getApprovedBy(),
//                updateAdDto.getState()
//        );
//    }
//
//    @Override
//    public Instant createdAt() {
//        return Instant.now();
//    }
}
