package com.marketplace.client.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Price {

  private BigDecimal amount;
  private String currencyCode;

}
