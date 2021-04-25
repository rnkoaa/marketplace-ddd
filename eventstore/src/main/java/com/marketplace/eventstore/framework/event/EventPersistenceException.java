package com.marketplace.eventstore.framework.event;

public class EventPersistenceException extends RuntimeException{

  public EventPersistenceException(String message) {
    super(message);
  }
}
