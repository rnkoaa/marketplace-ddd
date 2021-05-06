/*
 * This file is generated by jOOQ.
 */
package com.marketplace.eventstore.jdbc.tables.records;


import com.marketplace.eventstore.jdbc.tables.EventRecord;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EventRecordRecord extends UpdatableRecordImpl<EventRecordRecord> implements Record7<Integer, String, String, String, Integer, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>EVENT_RECORD.ID</code>.
     */
    public EventRecordRecord setId(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>EVENT_RECORD.ID</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>EVENT_RECORD.EVENT_ID</code>.
     */
    public EventRecordRecord setEventId(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>EVENT_RECORD.EVENT_ID</code>.
     */
    public String getEventId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>EVENT_RECORD.AGGREGATE_ID</code>.
     */
    public EventRecordRecord setAggregateId(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>EVENT_RECORD.AGGREGATE_ID</code>.
     */
    public String getAggregateId() {
        return (String) get(2);
    }

    /**
     * Setter for <code>EVENT_RECORD.EVENT_TYPE</code>.
     */
    public EventRecordRecord setEventType(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>EVENT_RECORD.EVENT_TYPE</code>.
     */
    public String getEventType() {
        return (String) get(3);
    }

    /**
     * Setter for <code>EVENT_RECORD.VERSION</code>.
     */
    public EventRecordRecord setVersion(Integer value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>EVENT_RECORD.VERSION</code>.
     */
    public Integer getVersion() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>EVENT_RECORD.DATA</code>.
     */
    public EventRecordRecord setData(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>EVENT_RECORD.DATA</code>.
     */
    public String getData() {
        return (String) get(5);
    }

    /**
     * Setter for <code>EVENT_RECORD.CREATED</code>.
     */
    public EventRecordRecord setCreated(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>EVENT_RECORD.CREATED</code>.
     */
    public String getCreated() {
        return (String) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<Integer, String, String, String, Integer, String, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<Integer, String, String, String, Integer, String, String> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return EventRecord.EVENT_RECORD.ID;
    }

    @Override
    public Field<String> field2() {
        return EventRecord.EVENT_RECORD.EVENT_ID;
    }

    @Override
    public Field<String> field3() {
        return EventRecord.EVENT_RECORD.AGGREGATE_ID;
    }

    @Override
    public Field<String> field4() {
        return EventRecord.EVENT_RECORD.EVENT_TYPE;
    }

    @Override
    public Field<Integer> field5() {
        return EventRecord.EVENT_RECORD.VERSION;
    }

    @Override
    public Field<String> field6() {
        return EventRecord.EVENT_RECORD.DATA;
    }

    @Override
    public Field<String> field7() {
        return EventRecord.EVENT_RECORD.CREATED;
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
        return getAggregateId();
    }

    @Override
    public String component4() {
        return getEventType();
    }

    @Override
    public Integer component5() {
        return getVersion();
    }

    @Override
    public String component6() {
        return getData();
    }

    @Override
    public String component7() {
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
        return getAggregateId();
    }

    @Override
    public String value4() {
        return getEventType();
    }

    @Override
    public Integer value5() {
        return getVersion();
    }

    @Override
    public String value6() {
        return getData();
    }

    @Override
    public String value7() {
        return getCreated();
    }

    @Override
    public EventRecordRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public EventRecordRecord value2(String value) {
        setEventId(value);
        return this;
    }

    @Override
    public EventRecordRecord value3(String value) {
        setAggregateId(value);
        return this;
    }

    @Override
    public EventRecordRecord value4(String value) {
        setEventType(value);
        return this;
    }

    @Override
    public EventRecordRecord value5(Integer value) {
        setVersion(value);
        return this;
    }

    @Override
    public EventRecordRecord value6(String value) {
        setData(value);
        return this;
    }

    @Override
    public EventRecordRecord value7(String value) {
        setCreated(value);
        return this;
    }

    @Override
    public EventRecordRecord values(Integer value1, String value2, String value3, String value4, Integer value5, String value6, String value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached EventRecordRecord
     */
    public EventRecordRecord() {
        super(EventRecord.EVENT_RECORD);
    }

    /**
     * Create a detached, initialised EventRecordRecord
     */
    public EventRecordRecord(Integer id, String eventId, String aggregateId, String eventType, Integer version, String data, String created) {
        super(EventRecord.EVENT_RECORD);

        setId(id);
        setEventId(eventId);
        setAggregateId(aggregateId);
        setEventType(eventType);
        setVersion(version);
        setData(data);
        setCreated(created);
    }
}
