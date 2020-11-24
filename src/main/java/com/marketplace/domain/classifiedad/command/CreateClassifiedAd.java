package com.marketplace.domain.classifiedad.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.marketplace.command.Command;
import com.marketplace.domain.classifiedad.ClassifiedAdState;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonDeserialize(builder = CreateClassifiedAd.CreateClassifiedAdBuilder.class)
public class CreateClassifiedAd implements Command {

  private UUID id;
  private String text;
  private String title;
  private PriceUpdate price;
  private UUID ownerId;
  private UUID approvedBy;
  private ClassifiedAdState state;


  @JsonPOJOBuilder(withPrefix = "")
  public static class CreateClassifiedAdBuilder {

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PriceUpdate {

    private String currencyCode;
    private BigDecimal bigDecimal;
  }
}
