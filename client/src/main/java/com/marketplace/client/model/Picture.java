package com.marketplace.client.model;

import lombok.Data;

@Data
public class Picture {

  private String id;
  private int with;
  private int height;
  private String uri;
  private int order;
}
