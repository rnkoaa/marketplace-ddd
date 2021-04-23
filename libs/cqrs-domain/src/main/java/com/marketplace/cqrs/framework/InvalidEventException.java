package com.marketplace.cqrs.framework;

public class InvalidEventException extends Throwable {
    public InvalidEventException(String message) {
        super(message);
    }
}
