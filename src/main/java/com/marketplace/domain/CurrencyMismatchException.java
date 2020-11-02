package com.marketplace.domain;

public class CurrencyMismatchException extends IllegalArgumentException {
    public CurrencyMismatchException(String message) {
        super(message);
    }
}
