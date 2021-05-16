package com.marketplace.domain.classifiedad;

public enum ClassifiedAdState {
  PENDING_REVIEW,
  ACTIVE,
  INACTIVE,
  MARKED_AS_SOLD,
  APPROVED;

  public static ClassifiedAdState fromString(String name) {

    return switch (name.toUpperCase()) {
      case "PENDING_REVIEW" -> PENDING_REVIEW;
      case "ACTIVE" -> ACTIVE;
      case "APPROVED" -> APPROVED;
      case "INACTIVE" -> INACTIVE;
      case "MARKED_AS_SOLD" -> MARKED_AS_SOLD;
      default -> INACTIVE;
    };
  }
}
