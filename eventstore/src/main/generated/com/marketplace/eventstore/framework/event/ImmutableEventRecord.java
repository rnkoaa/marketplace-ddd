package com.marketplace.eventstore.framework.event;

import com.google.common.base.MoreObjects;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.Var;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import org.immutables.value.Generated;

/**
 * Immutable implementation of {@link EventRecord}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableEventRecord.builder()}.
 */
@Generated(from = "EventRecord", generator = "Immutables")
@SuppressWarnings({"all"})
@ParametersAreNonnullByDefault
@javax.annotation.processing.Generated("org.immutables.processor.ProxyProcessor")
@Immutable
@CheckReturnValue
public final class ImmutableEventRecord extends EventRecord {
  private final String id;
  private final UUID eventId;
  private final String eventType;
  private final Event event;
  private final int version;

  private ImmutableEventRecord(
      String id,
      UUID eventId,
      String eventType,
      Event event,
      int version) {
    this.id = id;
    this.eventId = eventId;
    this.eventType = eventType;
    this.event = event;
    this.version = version;
  }

  /**
   *aggregateId 
   */
  @Override
  public String getId() {
    return id;
  }

  /**
   *id of the event 
   */
  @Override
  public UUID getEventId() {
    return eventId;
  }

  /**
   *the type of event or the className of the event 
   */
  @Override
  public String getEventType() {
    return eventType;
  }

  /**
   * @return The value of the {@code event} attribute
   */
  @Override
  public Event getEvent() {
    return event;
  }

  /**
   * @return The value of the {@code version} attribute
   */
  @Override
  public int getVersion() {
    return version;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link EventRecord#getId() id} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for id
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableEventRecord withId(String value) {
    String newValue = Objects.requireNonNull(value, "id");
    if (this.id.equals(newValue)) return this;
    return new ImmutableEventRecord(newValue, this.eventId, this.eventType, this.event, this.version);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link EventRecord#getEventId() eventId} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for eventId
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableEventRecord withEventId(UUID value) {
    if (this.eventId == value) return this;
    UUID newValue = Objects.requireNonNull(value, "eventId");
    return new ImmutableEventRecord(this.id, newValue, this.eventType, this.event, this.version);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link EventRecord#getEventType() eventType} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for eventType
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableEventRecord withEventType(String value) {
    String newValue = Objects.requireNonNull(value, "eventType");
    if (this.eventType.equals(newValue)) return this;
    return new ImmutableEventRecord(this.id, this.eventId, newValue, this.event, this.version);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link EventRecord#getEvent() event} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for event
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableEventRecord withEvent(Event value) {
    if (this.event == value) return this;
    Event newValue = Objects.requireNonNull(value, "event");
    return new ImmutableEventRecord(this.id, this.eventId, this.eventType, newValue, this.version);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link EventRecord#getVersion() version} attribute.
   * A value equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for version
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableEventRecord withVersion(int value) {
    if (this.version == value) return this;
    return new ImmutableEventRecord(this.id, this.eventId, this.eventType, this.event, value);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableEventRecord} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another) return true;
    return another instanceof ImmutableEventRecord
        && equalTo((ImmutableEventRecord) another);
  }

  private boolean equalTo(ImmutableEventRecord another) {
    return id.equals(another.id)
        && eventId.equals(another.eventId)
        && eventType.equals(another.eventType)
        && event.equals(another.event)
        && version == another.version;
  }

  /**
   * Computes a hash code from attributes: {@code id}, {@code eventId}, {@code eventType}, {@code event}, {@code version}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    @Var int h = 5381;
    h += (h << 5) + id.hashCode();
    h += (h << 5) + eventId.hashCode();
    h += (h << 5) + eventType.hashCode();
    h += (h << 5) + event.hashCode();
    h += (h << 5) + version;
    return h;
  }

  /**
   * Prints the immutable value {@code EventRecord} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper("EventRecord")
        .omitNullValues()
        .add("id", id)
        .add("eventId", eventId)
        .add("eventType", eventType)
        .add("event", event)
        .add("version", version)
        .toString();
  }

  /**
   * Creates an immutable copy of a {@link EventRecord} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable EventRecord instance
   */
  public static ImmutableEventRecord copyOf(EventRecord instance) {
    if (instance instanceof ImmutableEventRecord) {
      return (ImmutableEventRecord) instance;
    }
    return ImmutableEventRecord.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableEventRecord ImmutableEventRecord}.
   * <pre>
   * ImmutableEventRecord.builder()
   *    .id(String) // required {@link EventRecord#getId() id}
   *    .eventId(UUID) // required {@link EventRecord#getEventId() eventId}
   *    .eventType(String) // required {@link EventRecord#getEventType() eventType}
   *    .event(com.marketplace.eventstore.framework.event.Event) // required {@link EventRecord#getEvent() event}
   *    .version(int) // required {@link EventRecord#getVersion() version}
   *    .build();
   * </pre>
   * @return A new ImmutableEventRecord builder
   */
  public static ImmutableEventRecord.Builder builder() {
    return new ImmutableEventRecord.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableEventRecord ImmutableEventRecord}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  @Generated(from = "EventRecord", generator = "Immutables")
  @NotThreadSafe
  public static final class Builder {
    private static final long INIT_BIT_ID = 0x1L;
    private static final long INIT_BIT_EVENT_ID = 0x2L;
    private static final long INIT_BIT_EVENT_TYPE = 0x4L;
    private static final long INIT_BIT_EVENT = 0x8L;
    private static final long INIT_BIT_VERSION = 0x10L;
    private long initBits = 0x1fL;

    private @Nullable String id;
    private @Nullable UUID eventId;
    private @Nullable String eventType;
    private @Nullable Event event;
    private int version;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code EventRecord} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    @CanIgnoreReturnValue 
    public final Builder from(EventRecord instance) {
      Objects.requireNonNull(instance, "instance");
      id(instance.getId());
      eventId(instance.getEventId());
      eventType(instance.getEventType());
      event(instance.getEvent());
      version(instance.getVersion());
      return this;
    }

    /**
     * Initializes the value for the {@link EventRecord#getId() id} attribute.
     * @param id The value for id 
     * @return {@code this} builder for use in a chained invocation
     */
    @CanIgnoreReturnValue 
    public final Builder id(String id) {
      this.id = Objects.requireNonNull(id, "id");
      initBits &= ~INIT_BIT_ID;
      return this;
    }

    /**
     * Initializes the value for the {@link EventRecord#getEventId() eventId} attribute.
     * @param eventId The value for eventId 
     * @return {@code this} builder for use in a chained invocation
     */
    @CanIgnoreReturnValue 
    public final Builder eventId(UUID eventId) {
      this.eventId = Objects.requireNonNull(eventId, "eventId");
      initBits &= ~INIT_BIT_EVENT_ID;
      return this;
    }

    /**
     * Initializes the value for the {@link EventRecord#getEventType() eventType} attribute.
     * @param eventType The value for eventType 
     * @return {@code this} builder for use in a chained invocation
     */
    @CanIgnoreReturnValue 
    public final Builder eventType(String eventType) {
      this.eventType = Objects.requireNonNull(eventType, "eventType");
      initBits &= ~INIT_BIT_EVENT_TYPE;
      return this;
    }

    /**
     * Initializes the value for the {@link EventRecord#getEvent() event} attribute.
     * @param event The value for event 
     * @return {@code this} builder for use in a chained invocation
     */
    @CanIgnoreReturnValue 
    public final Builder event(Event event) {
      this.event = Objects.requireNonNull(event, "event");
      initBits &= ~INIT_BIT_EVENT;
      return this;
    }

    /**
     * Initializes the value for the {@link EventRecord#getVersion() version} attribute.
     * @param version The value for version 
     * @return {@code this} builder for use in a chained invocation
     */
    @CanIgnoreReturnValue 
    public final Builder version(int version) {
      this.version = version;
      initBits &= ~INIT_BIT_VERSION;
      return this;
    }

    /**
     * Builds a new {@link ImmutableEventRecord ImmutableEventRecord}.
     * @return An immutable instance of EventRecord
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableEventRecord build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableEventRecord(id, eventId, eventType, event, version);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<>();
      if ((initBits & INIT_BIT_ID) != 0) attributes.add("id");
      if ((initBits & INIT_BIT_EVENT_ID) != 0) attributes.add("eventId");
      if ((initBits & INIT_BIT_EVENT_TYPE) != 0) attributes.add("eventType");
      if ((initBits & INIT_BIT_EVENT) != 0) attributes.add("event");
      if ((initBits & INIT_BIT_VERSION) != 0) attributes.add("version");
      return "Cannot build EventRecord, some of required attributes are not set " + attributes;
    }
  }
}
