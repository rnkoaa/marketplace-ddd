package com.marketplace.framework;

public class Strings {

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty() || value.isBlank();
    }
}
