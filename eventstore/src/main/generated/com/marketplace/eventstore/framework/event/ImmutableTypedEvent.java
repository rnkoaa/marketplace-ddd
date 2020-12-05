package com.marketplace.eventstore.framework.event;

import com.google.common.base.MoreObjects;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.Var;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import org.immutables.value.Generated;

/**
 * Immutable implementation of {@link TypedEvent}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableTypedEvent.builder()}.
 */
@Generated(from = "TypedEvent", generator = "Immutables")
@SuppressWarnings({"all"})
@ParametersAreNonnullByDefault
@javax.annotation.processing.Generated("org.immutables.processor.ProxyProcessor")
@Immutable
@CheckReturnValue
public final class ImmutableTypedEvent implements TypedEvent {
  private final String type;
  private final int sequenceId;
  private final String eventBody;

  private ImmutableTypedEvent(String type, int sequenceId, String eventBody) {
    this.type = type;
    this.sequenceId = sequenceId;
    this.eventBody = eventBody;
  }

  /**
   * @return The value of the {@code type} attribute
   */
  @Override
  public String getType() {
    return type;
  }

  /**
   * @return The value of the {@code sequenceId} attribute
   */
  @Override
  public int getSequenceId() {
    return sequenceId;
  }

  /**
   * @return The value of the {@code eventBody} attribute
   */
  @Override
  public String getEventBody() {
    return eventBody;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link TypedEvent#getType() type} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for type
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableTypedEvent withType(String value) {
    String newValue = Objects.requireNonNull(value, "type");
    if (this.type.equals(newValue)) return this;
    return new ImmutableTypedEvent(newValue, this.sequenceId, this.eventBody);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link TypedEvent#getSequenceId() sequenceId} attribute.
   * A value equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for sequenceId
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableTypedEvent withSequenceId(int value) {
    if (this.sequenceId == value) return this;
    return new ImmutableTypedEvent(this.type, value, this.eventBody);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link TypedEvent#getEventBody() eventBody} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for eventBody
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableTypedEvent withEventBody(String value) {
    String newValue = Objects.requireNonNull(value, "eventBody");
    if (this.eventBody.equals(newValue)) return this;
    return new ImmutableTypedEvent(this.type, this.sequenceId, newValue);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableTypedEvent} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another) return true;
    return another instanceof ImmutableTypedEvent
        && equalTo((ImmutableTypedEvent) another);
  }

  private boolean equalTo(ImmutableTypedEvent another) {
    return type.equals(another.type)
        && sequenceId == another.sequenceId
        && eventBody.equals(another.eventBody);
  }

  /**
   * Computes a hash code from attributes: {@code type}, {@code sequenceId}, {@code eventBody}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    @Var int h = 5381;
    h += (h << 5) + type.hashCode();
    h += (h << 5) + sequenceId;
    h += (h << 5) + eventBody.hashCode();
    return h;
  }

  /**
   * Prints the immutable value {@code TypedEvent} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper("TypedEvent")
        .omitNullValues()
        .add("type", type)
        .add("sequenceId", sequenceId)
        .add("eventBody", eventBody)
        .toString();
  }

  /**
   * Creates an immutable copy of a {@link TypedEvent} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable TypedEvent instance
   */
  public static ImmutableTypedEvent copyOf(TypedEvent instance) {
    if (instance instanceof ImmutableTypedEvent) {
      return (ImmutableTypedEvent) instance;
    }
    return ImmutableTypedEvent.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableTypedEvent ImmutableTypedEvent}.
   * <pre>
   * ImmutableTypedEvent.builder()
   *    .type(String) // required {@link TypedEvent#getType() type}
   *    .sequenceId(int) // required {@link TypedEvent#getSequenceId() sequenceId}
   *    .eventBody(String) // required {@link TypedEvent#getEventBody() eventBody}
   *    .build();
   * </pre>
   * @return A new ImmutableTypedEvent builder
   */
  public static ImmutableTypedEvent.Builder builder() {
    return new ImmutableTypedEvent.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableTypedEvent ImmutableTypedEvent}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  @Generated(from = "TypedEvent", generator = "Immutables")
  @NotThreadSafe
  public static final class Builder {
    private static final long INIT_BIT_TYPE = 0x1L;
    private static final long INIT_BIT_SEQUENCE_ID = 0x2L;
    private static final long INIT_BIT_EVENT_BODY = 0x4L;
    private long initBits = 0x7L;

    private @Nullable String type;
    private int sequenceId;
    private @Nullable String eventBody;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code TypedEvent} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    @CanIgnoreReturnValue 
    public final Builder from(TypedEvent instance) {
      Objects.requireNonNull(instance, "instance");
      type(instance.getType());
      sequenceId(instance.getSequenceId());
      eventBody(instance.getEventBody());
      return this;
    }

    /**
     * Initializes the value for the {@link TypedEvent#getType() type} attribute.
     * @param type The value for type 
     * @return {@code this} builder for use in a chained invocation
     */
    @CanIgnoreReturnValue 
    public final Builder type(String type) {
      this.type = Objects.requireNonNull(type, "type");
      initBits &= ~INIT_BIT_TYPE;
      return this;
    }

    /**
     * Initializes the value for the {@link TypedEvent#getSequenceId() sequenceId} attribute.
     * @param sequenceId The value for sequenceId 
     * @return {@code this} builder for use in a chained invocation
     */
    @CanIgnoreReturnValue 
    public final Builder sequenceId(int sequenceId) {
      this.sequenceId = sequenceId;
      initBits &= ~INIT_BIT_SEQUENCE_ID;
      return this;
    }

    /**
     * Initializes the value for the {@link TypedEvent#getEventBody() eventBody} attribute.
     * @param eventBody The value for eventBody 
     * @return {@code this} builder for use in a chained invocation
     */
    @CanIgnoreReturnValue 
    public final Builder eventBody(String eventBody) {
      this.eventBody = Objects.requireNonNull(eventBody, "eventBody");
      initBits &= ~INIT_BIT_EVENT_BODY;
      return this;
    }

    /**
     * Builds a new {@link ImmutableTypedEvent ImmutableTypedEvent}.
     * @return An immutable instance of TypedEvent
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableTypedEvent build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableTypedEvent(type, sequenceId, eventBody);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<>();
      if ((initBits & INIT_BIT_TYPE) != 0) attributes.add("type");
      if ((initBits & INIT_BIT_SEQUENCE_ID) != 0) attributes.add("sequenceId");
      if ((initBits & INIT_BIT_EVENT_BODY) != 0) attributes.add("eventBody");
      return "Cannot build TypedEvent, some of required attributes are not set " + attributes;
    }
  }
}
