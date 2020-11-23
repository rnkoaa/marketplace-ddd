package com.marketplace.domain.classifiedad.command;

import com.marketplace.command.Command;
import com.marketplace.domain.classifiedad.ClassifiedAdState;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
  private List<PictureDto> pictures;
  private ClassifiedAdState state;


  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class PictureDto {
    private UUID id;
    private int order;
    private String uri;
    private int width;
    private int height;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class PriceDto {

    private String currencyCode;
    private BigDecimal amount;
  }
}
