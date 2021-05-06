/*
 * This file is generated by jOOQ.
 */
package com.marketplace.eventstore.jdbc.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EventRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Integer id;
    private final String  eventId;
    private final String  aggregateId;
    private final String  eventType;
    private final Integer version;
    private final String  data;
    private final String  created;

    public EventRecord(EventRecord value) {
        this.id = value.id;
        this.eventId = value.eventId;
        this.aggregateId = value.aggregateId;
        this.eventType = value.eventType;
        this.version = value.version;
        this.data = value.data;
        this.created = value.created;
    }

    public EventRecord(
        Integer id,
        String  eventId,
        String  aggregateId,
        String  eventType,
        Integer version,
        String  data,
        String  created
    ) {
        this.id = id;
        this.eventId = eventId;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.version = version;
        this.data = data;
        this.created = created;
    }

    /**
     * Getter for <code>EVENT_RECORD.ID</code>.
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Getter for <code>EVENT_RECORD.EVENT_ID</code>.
     */
    public String getEventId() {
        return this.eventId;
    }

    /**
     * Getter for <code>EVENT_RECORD.AGGREGATE_ID</code>.
     */
    public String getAggregateId() {
        return this.aggregateId;
    }

    /**
     * Getter for <code>EVENT_RECORD.EVENT_TYPE</code>.
     */
    public String getEventType() {
        return this.eventType;
    }

    /**
     * Getter for <code>EVENT_RECORD.VERSION</code>.
     */
    public Integer getVersion() {
        return this.version;
    }

    /**
     * Getter for <code>EVENT_RECORD.DATA</code>.
     */
    public String getData() {
        return this.data;
    }

    /**
     * Getter for <code>EVENT_RECORD.CREATED</code>.
     */
    public String getCreated() {
        return this.created;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("EventRecord (");

        sb.append(id);
        sb.append(", ").append(eventId);
        sb.append(", ").append(aggregateId);
        sb.append(", ").append(eventType);
        sb.append(", ").append(version);
        sb.append(", ").append(data);
        sb.append(", ").append(created);

        sb.append(")");
        return sb.toString();
    }
}
