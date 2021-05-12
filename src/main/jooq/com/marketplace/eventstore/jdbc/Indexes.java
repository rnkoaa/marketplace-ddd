/*
 * This file is generated by jOOQ.
 */
package com.marketplace.eventstore.jdbc;


import com.marketplace.eventstore.jdbc.tables.EventData;
import com.marketplace.eventstore.jdbc.tables.FlywaySchemaHistory;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling indexes of tables in the default schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index FLYWAY_SCHEMA_HISTORY_S_IDX = Internal.createIndex(DSL.name("flyway_schema_history_s_idx"), FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, new OrderField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.SUCCESS }, false);
    public static final Index IDX_EVENT_DATA_AGGREGATE_ID = Internal.createIndex(DSL.name("idx_event_data_aggregate_id"), EventData.EVENT_DATA, new OrderField[] { EventData.EVENT_DATA.AGGREGATE_ID }, false);
    public static final Index IDX_EVENT_DATA_AGGREGATE_NAME = Internal.createIndex(DSL.name("idx_event_data_aggregate_name"), EventData.EVENT_DATA, new OrderField[] { EventData.EVENT_DATA.AGGREGATE_NAME }, false);
    public static final Index IDX_EVENT_DATA_EVENT_ID = Internal.createIndex(DSL.name("idx_event_data_event_id"), EventData.EVENT_DATA, new OrderField[] { EventData.EVENT_DATA.EVENT_ID }, true);
}
