package com.marketplace.client.model;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class ClassifiedAdResponse {

  private UUID id;
  private UUID ownerId;
  private UUID approver;
  private String title;
  private String text;
  private Price price;
  private List<Picture> pictures;
  private ClassifiedAdState state;

}
