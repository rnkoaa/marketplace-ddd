package com.marketplace.domain.classifiedad;

import com.marketplace.annotations.MongoSingleRecordValue;

@MongoSingleRecordValue
public record ClassifiedAdText( String value) {
    public ClassifiedAdText {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("value cannot be empty or null");
        }
    }

    public static ClassifiedAdText fromString(String value) {
        return new ClassifiedAdText(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
