package com.marketplace.domain.classifiedad;

public record Currency(String code, boolean inUse, int decimalPlaces) {

    public boolean isNone() {
        return !inUse;
    }

    public static Currency none() {
        return new Currency("", false, 0);
    }

}
