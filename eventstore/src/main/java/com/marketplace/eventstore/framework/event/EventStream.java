package com.marketplace.eventstore.framework.event;

import java.time.Instant;
import java.util.List;

public interface EventStream<T> {
  /**
   * The Name of the stream This is an optional field
   *
   * @return name of the stream
   */
  String getName();

  /**
   * The id of the stream, usually the aggregateId of the stream
   *
   * @return aggregateId of the stream
   */
  String getId();

  /** @return latest version of the stream */
  long getVersion();

  /** @return all events that make up the stream sorted in ascending order */
  List<T> getEvents();

  /**
   * Append an event to the stream with the expectedVersion of the current stream
   *
   * @param entity event to be appended
   * @param expectedVersion the current version of the stream
   */
  void append(T entity, long expectedVersion);

  /** @return the number of events in the current stream */
  int size();

  /** @return time when the stream was started, this value should never be updated. */
  default Instant createdAt() {
    return Instant.now();
  }

  /**
   * @return the time when the stream was updated, this value can be updated anytime an event is
   *     added to the stream
   */
  default Instant updatedAt() {
    return Instant.now();
  }
}
