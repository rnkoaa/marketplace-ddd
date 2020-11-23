package com.marketplace.client.model;

import lombok.Data;

@Data
public class Picture {

  private String id;
  private int width;
  private int height;
  private String uri;
  private int order;
}
