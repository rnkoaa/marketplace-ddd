package com.marketplace.cqrs.framework;

public class Strings {

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty() || value.isBlank();
    }
}
