/*
 * This file is generated by jOOQ.
 */
package com.marketplace.eventstore.jdbc.tables;


import com.marketplace.eventstore.jdbc.DefaultSchema;
import com.marketplace.eventstore.jdbc.Keys;
import com.marketplace.eventstore.jdbc.tables.records.EventRecordRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row7;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EventRecord extends TableImpl<EventRecordRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>EVENT_RECORD</code>
     */
    public static final EventRecord EVENT_RECORD = new EventRecord();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EventRecordRecord> getRecordType() {
        return EventRecordRecord.class;
    }

    /**
     * The column <code>EVENT_RECORD.ID</code>.
     */
    public final TableField<EventRecordRecord, Integer> ID = createField(DSL.name("ID"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>EVENT_RECORD.EVENT_ID</code>.
     */
    public final TableField<EventRecordRecord, String> EVENT_ID = createField(DSL.name("EVENT_ID"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>EVENT_RECORD.AGGREGATE_ID</code>.
     */
    public final TableField<EventRecordRecord, String> AGGREGATE_ID = createField(DSL.name("AGGREGATE_ID"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>EVENT_RECORD.EVENT_TYPE</code>.
     */
    public final TableField<EventRecordRecord, String> EVENT_TYPE = createField(DSL.name("EVENT_TYPE"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>EVENT_RECORD.VERSION</code>.
     */
    public final TableField<EventRecordRecord, Integer> VERSION = createField(DSL.name("VERSION"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>EVENT_RECORD.DATA</code>.
     */
    public final TableField<EventRecordRecord, String> DATA = createField(DSL.name("DATA"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>EVENT_RECORD.CREATED</code>.
     */
    public final TableField<EventRecordRecord, String> CREATED = createField(DSL.name("CREATED"), SQLDataType.CLOB.nullable(false), this, "");

    private EventRecord(Name alias, Table<EventRecordRecord> aliased) {
        this(alias, aliased, null);
    }

    private EventRecord(Name alias, Table<EventRecordRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>EVENT_RECORD</code> table reference
     */
    public EventRecord(String alias) {
        this(DSL.name(alias), EVENT_RECORD);
    }

    /**
     * Create an aliased <code>EVENT_RECORD</code> table reference
     */
    public EventRecord(Name alias) {
        this(alias, EVENT_RECORD);
    }

    /**
     * Create a <code>EVENT_RECORD</code> table reference
     */
    public EventRecord() {
        this(DSL.name("EVENT_RECORD"), null);
    }

    public <O extends Record> EventRecord(Table<O> child, ForeignKey<O, EventRecordRecord> key) {
        super(child, key, EVENT_RECORD);
    }

    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public UniqueKey<EventRecordRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_6;
    }

    @Override
    public List<UniqueKey<EventRecordRecord>> getKeys() {
        return Arrays.<UniqueKey<EventRecordRecord>>asList(Keys.CONSTRAINT_6);
    }

    @Override
    public EventRecord as(String alias) {
        return new EventRecord(DSL.name(alias), this);
    }

    @Override
    public EventRecord as(Name alias) {
        return new EventRecord(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EventRecord rename(String name) {
        return new EventRecord(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public EventRecord rename(Name name) {
        return new EventRecord(name, null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<Integer, String, String, String, Integer, String, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }
}
