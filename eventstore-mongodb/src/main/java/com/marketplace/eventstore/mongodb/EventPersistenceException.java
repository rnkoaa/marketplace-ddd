package com.marketplace.eventstore.mongodb;

public class EventPersistenceException extends RuntimeException{

  public EventPersistenceException(String message) {
    super(message);
  }
}
