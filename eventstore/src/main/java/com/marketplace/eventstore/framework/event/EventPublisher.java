package com.marketplace.eventstore.framework.event;

import java.util.List;

public interface EventPublisher<T> {

  /**
   * publish event to any subscribers, this will be called after the event has been appended to the
   * stream
   *
   * @param streamId id of the stream to which this event was appended to.
   * @param event the event which is being acted upon
   */
  void publish(String streamId, T event);

  /**
   * publish events to any subscribers, this will be called after the event has been appended to the
   * stream
   *
   * @param streamId id of the stream to which this event was appended to.
   * @param event the events which are being acted upon
   */
  void publish(String streamId, List<T> event);
}
