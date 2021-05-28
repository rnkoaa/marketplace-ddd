package com.marketplace.domain.userprofile.controller;

public class DuplicateDisplayNameException extends RuntimeException {

    public DuplicateDisplayNameException(String message) {
        super(message);
    }
}
