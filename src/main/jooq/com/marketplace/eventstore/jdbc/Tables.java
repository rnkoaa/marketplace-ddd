/*
 * This file is generated by jOOQ.
 */
package com.marketplace.eventstore.jdbc;


import com.marketplace.eventstore.jdbc.tables.ClassifiedAd;
import com.marketplace.eventstore.jdbc.tables.EventData;
import com.marketplace.eventstore.jdbc.tables.FlywaySchemaHistory;
import com.marketplace.eventstore.jdbc.tables.UserProfile;


/**
 * Convenience access to all tables in the default schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>classified_ad</code>.
     */
    public static final ClassifiedAd CLASSIFIED_AD = ClassifiedAd.CLASSIFIED_AD;

    /**
     * The table <code>event_data</code>.
     */
    public static final EventData EVENT_DATA = EventData.EVENT_DATA;

    /**
     * The table <code>flyway_schema_history</code>.
     */
    public static final FlywaySchemaHistory FLYWAY_SCHEMA_HISTORY = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY;

    /**
     * The table <code>user_profile</code>.
     */
    public static final UserProfile USER_PROFILE = UserProfile.USER_PROFILE;
}
