package com.marketplace.domain;

public class InvalidStateException extends IllegalArgumentException {
    public InvalidStateException(String message) {
        super(message);
    }
}
