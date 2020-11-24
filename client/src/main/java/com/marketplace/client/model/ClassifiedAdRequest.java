package com.marketplace.client.model;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassifiedAdRequest {

  private UUID id;
  private UUID ownerId;
  private UUID approvedBy;
  private String title;
  private String text;
  private Price price;
  private List<Picture> pictures;
}
