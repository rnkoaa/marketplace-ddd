package com.marketplace.eventstore.framework.event;

public class InvalidVersionException extends RuntimeException{

    public InvalidVersionException(String message) {
        super(message);
    }
}
