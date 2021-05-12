/*
 * This file is generated by jOOQ.
 */
package com.marketplace.eventstore.jdbc.tables.records;


import com.marketplace.eventstore.jdbc.tables.EventData;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EventDataRecord extends UpdatableRecordImpl<EventDataRecord> implements Record8<Integer, String, String, String, String, Integer, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>event_data.id</code>.
     */
    public EventDataRecord setId(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>event_data.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>event_data.event_id</code>.
     */
    public EventDataRecord setEventId(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>event_data.event_id</code>.
     */
    public String getEventId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>event_data.aggregate_name</code>.
     */
    public EventDataRecord setAggregateName(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>event_data.aggregate_name</code>.
     */
    public String getAggregateName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>event_data.aggregate_id</code>.
     */
    public EventDataRecord setAggregateId(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>event_data.aggregate_id</code>.
     */
    public String getAggregateId() {
        return (String) get(3);
    }

    /**
     * Setter for <code>event_data.event_type</code>.
     */
    public EventDataRecord setEventType(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>event_data.event_type</code>.
     */
    public String getEventType() {
        return (String) get(4);
    }

    /**
     * Setter for <code>event_data.event_version</code>.
     */
    public EventDataRecord setEventVersion(Integer value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>event_data.event_version</code>.
     */
    public Integer getEventVersion() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>event_data.data</code>.
     */
    public EventDataRecord setData(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>event_data.data</code>.
     */
    public String getData() {
        return (String) get(6);
    }

    /**
     * Setter for <code>event_data.created</code>.
     */
    public EventDataRecord setCreated(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>event_data.created</code>.
     */
    public String getCreated() {
        return (String) get(7);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row8<Integer, String, String, String, String, Integer, String, String> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    @Override
    public Row8<Integer, String, String, String, String, Integer, String, String> valuesRow() {
        return (Row8) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return EventData.EVENT_DATA.ID;
    }

    @Override
    public Field<String> field2() {
        return EventData.EVENT_DATA.EVENT_ID;
    }

    @Override
    public Field<String> field3() {
        return EventData.EVENT_DATA.AGGREGATE_NAME;
    }

    @Override
    public Field<String> field4() {
        return EventData.EVENT_DATA.AGGREGATE_ID;
    }

    @Override
    public Field<String> field5() {
        return EventData.EVENT_DATA.EVENT_TYPE;
    }

    @Override
    public Field<Integer> field6() {
        return EventData.EVENT_DATA.EVENT_VERSION;
    }

    @Override
    public Field<String> field7() {
        return EventData.EVENT_DATA.DATA;
    }

    @Override
    public Field<String> field8() {
        return EventData.EVENT_DATA.CREATED;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getEventId();
    }

    @Override
    public String component3() {
        return getAggregateName();
    }

    @Override
    public String component4() {
        return getAggregateId();
    }

    @Override
    public String component5() {
        return getEventType();
    }

    @Override
    public Integer component6() {
        return getEventVersion();
    }

    @Override
    public String component7() {
        return getData();
    }

    @Override
    public String component8() {
        return getCreated();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getEventId();
    }

    @Override
    public String value3() {
        return getAggregateName();
    }

    @Override
    public String value4() {
        return getAggregateId();
    }

    @Override
    public String value5() {
        return getEventType();
    }

    @Override
    public Integer value6() {
        return getEventVersion();
    }

    @Override
    public String value7() {
        return getData();
    }

    @Override
    public String value8() {
        return getCreated();
    }

    @Override
    public EventDataRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public EventDataRecord value2(String value) {
        setEventId(value);
        return this;
    }

    @Override
    public EventDataRecord value3(String value) {
        setAggregateName(value);
        return this;
    }

    @Override
    public EventDataRecord value4(String value) {
        setAggregateId(value);
        return this;
    }

    @Override
    public EventDataRecord value5(String value) {
        setEventType(value);
        return this;
    }

    @Override
    public EventDataRecord value6(Integer value) {
        setEventVersion(value);
        return this;
    }

    @Override
    public EventDataRecord value7(String value) {
        setData(value);
        return this;
    }

    @Override
    public EventDataRecord value8(String value) {
        setCreated(value);
        return this;
    }

    @Override
    public EventDataRecord values(Integer value1, String value2, String value3, String value4, String value5, Integer value6, String value7, String value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached EventDataRecord
     */
    public EventDataRecord() {
        super(EventData.EVENT_DATA);
    }

    /**
     * Create a detached, initialised EventDataRecord
     */
    public EventDataRecord(Integer id, String eventId, String aggregateName, String aggregateId, String eventType, Integer eventVersion, String data, String created) {
        super(EventData.EVENT_DATA);

        setId(id);
        setEventId(eventId);
        setAggregateName(aggregateName);
        setAggregateId(aggregateId);
        setEventType(eventType);
        setEventVersion(eventVersion);
        setData(data);
        setCreated(created);
    }
}
