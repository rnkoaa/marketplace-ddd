package com.marketplace.domain.classifiedad;

public enum ClassifiedAdState {
    pendingReview,
    active,
    inactive,
    markedAsSold, approved;

    static ClassifiedAdState fromString(String name) {
        return switch (name) {
            case "pendingReview" -> pendingReview;
            case "active" -> active;
            case "inactive" -> inactive;
            case "markedAsSold" -> markedAsSold;
            default -> inactive;
        };
    }
}
