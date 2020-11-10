package com.marketplace.domain.classifiedad;

public class CurrencyMismatchException extends IllegalArgumentException {
    public CurrencyMismatchException(String message) {
        super(message);
    }
}
