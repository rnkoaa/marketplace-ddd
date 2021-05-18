/*
 * This file is generated by jOOQ.
 */
package com.marketplace.eventstore.jdbc.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EventData implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String  id;
    private final String  aggregateName;
    private final String  aggregateId;
    private final String  eventType;
    private final Integer eventVersion;
    private final String  data;
    private final String  created;

    public EventData(EventData value) {
        this.id = value.id;
        this.aggregateName = value.aggregateName;
        this.aggregateId = value.aggregateId;
        this.eventType = value.eventType;
        this.eventVersion = value.eventVersion;
        this.data = value.data;
        this.created = value.created;
    }

    public EventData(
        String  id,
        String  aggregateName,
        String  aggregateId,
        String  eventType,
        Integer eventVersion,
        String  data,
        String  created
    ) {
        this.id = id;
        this.aggregateName = aggregateName;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.eventVersion = eventVersion;
        this.data = data;
        this.created = created;
    }

    /**
     * Getter for <code>event_data.id</code>.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Getter for <code>event_data.aggregate_name</code>.
     */
    public String getAggregateName() {
        return this.aggregateName;
    }

    /**
     * Getter for <code>event_data.aggregate_id</code>.
     */
    public String getAggregateId() {
        return this.aggregateId;
    }

    /**
     * Getter for <code>event_data.event_type</code>.
     */
    public String getEventType() {
        return this.eventType;
    }

    /**
     * Getter for <code>event_data.event_version</code>.
     */
    public Integer getEventVersion() {
        return this.eventVersion;
    }

    /**
     * Getter for <code>event_data.data</code>.
     */
    public String getData() {
        return this.data;
    }

    /**
     * Getter for <code>event_data.created</code>.
     */
    public String getCreated() {
        return this.created;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("EventData (");

        sb.append(id);
        sb.append(", ").append(aggregateName);
        sb.append(", ").append(aggregateId);
        sb.append(", ").append(eventType);
        sb.append(", ").append(eventVersion);
        sb.append(", ").append(data);
        sb.append(", ").append(created);

        sb.append(")");
        return sb.toString();
    }
}
