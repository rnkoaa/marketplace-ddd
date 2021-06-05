package com.marketplace.domain.classifiedad;

import java.util.Objects;

public record ClassifiedAdText(String value) {
    static ClassifiedAdText DEFAULT = new ClassifiedAdText("");

    public ClassifiedAdText {
        Objects.requireNonNull(value, "classifiedAd text cannot be null");
    }

    public static ClassifiedAdText from(String value) {
        return new ClassifiedAdText(value);
    }

    boolean isValid() {
        return !value.isEmpty();
    }

    @Override
    public String toString() {
        return value;
    }
}
