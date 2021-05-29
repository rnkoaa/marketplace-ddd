/*
 * This file is generated by jOOQ.
 */
package com.marketplace.evenstore.jooq;


import com.marketplace.evenstore.jooq.tables.ClassifiedAd;
import com.marketplace.evenstore.jooq.tables.EventData;
import com.marketplace.evenstore.jooq.tables.FlywaySchemaHistory;
import com.marketplace.evenstore.jooq.tables.UserProfile;

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
    public static final Index IDX_CLASSIFIED_AD_APPROVER_ID = Internal.createIndex(DSL.name("idx_classified_ad_approver_id"), ClassifiedAd.CLASSIFIED_AD, new OrderField[] { ClassifiedAd.CLASSIFIED_AD.APPROVER }, false);
    public static final Index IDX_CLASSIFIED_AD_OWNER_ID = Internal.createIndex(DSL.name("idx_classified_ad_owner_id"), ClassifiedAd.CLASSIFIED_AD, new OrderField[] { ClassifiedAd.CLASSIFIED_AD.OWNER }, false);
    public static final Index IDX_EVENT_DATA_AGGREGATE_ID = Internal.createIndex(DSL.name("idx_event_data_aggregate_id"), EventData.EVENT_DATA, new OrderField[] { EventData.EVENT_DATA.AGGREGATE_ID }, false);
    public static final Index IDX_EVENT_DATA_AGGREGATE_NAME = Internal.createIndex(DSL.name("idx_event_data_aggregate_name"), EventData.EVENT_DATA, new OrderField[] { EventData.EVENT_DATA.AGGREGATE_NAME }, false);
    public static final Index IDX_EVENT_DATA_STREAM_ID = Internal.createIndex(DSL.name("idx_event_data_stream_id"), EventData.EVENT_DATA, new OrderField[] { EventData.EVENT_DATA.STREAM_ID }, false);
    public static final Index IDX_EVENT_DATA_STREAM_ID_VERSION = Internal.createIndex(DSL.name("idx_event_data_stream_id_version"), EventData.EVENT_DATA, new OrderField[] { EventData.EVENT_DATA.STREAM_ID, EventData.EVENT_DATA.EVENT_VERSION }, false);
    public static final Index IDX_USER_PROFILE_DISPLAYNAME = Internal.createIndex(DSL.name("idx_user_profile_displayName"), UserProfile.USER_PROFILE, new OrderField[] { UserProfile.USER_PROFILE.DISPLAY_NAME }, true);
}