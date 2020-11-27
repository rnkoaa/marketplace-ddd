package com.marketplace.eventstore.framework;

public class UnExpectedVersionException extends RuntimeException {
    public UnExpectedVersionException(String message) {
        super(message);
    }
}