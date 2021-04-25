package com.marketplace.domain.classifiedad;

import java.util.UUID;

public record ClassifiedAdId(UUID id) {

  public ClassifiedAdId {
    if (id == null) {
      throw new IllegalArgumentException("id cannot be null");
    }
  }

  public ClassifiedAdId() {
    this(UUID.randomUUID());
  }

  public static ClassifiedAdId newClassifedAdId() {
    return new ClassifiedAdId(UUID.randomUUID());
  }

  public static ClassifiedAdId from(UUID value) {
    return new ClassifiedAdId(value);
  }

  @Override
  public String toString() {
    return id.toString();
  }

  public static ClassifiedAdId fromString(String value) {
    return new ClassifiedAdId(UUID.fromString(value));
  }

}
