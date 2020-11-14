package com.marketplace.domain.userprofile;

import com.marketplace.framework.Strings;

public record DisplayName(String value) {
    public DisplayName {
        if (Strings.isNullOrEmpty(value)) {
            throw new IllegalArgumentException("display name cannot be null or empty");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
