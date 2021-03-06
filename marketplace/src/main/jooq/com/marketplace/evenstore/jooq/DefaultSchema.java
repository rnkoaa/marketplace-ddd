/*
 * This file is generated by jOOQ.
 */
package com.marketplace.evenstore.jooq;


import com.marketplace.evenstore.jooq.tables.ClassCache;
import com.marketplace.evenstore.jooq.tables.ClassifiedAd;
import com.marketplace.evenstore.jooq.tables.EventData;
import com.marketplace.evenstore.jooq.tables.FlywaySchemaHistory;
import com.marketplace.evenstore.jooq.tables.UserProfile;

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DefaultSchema extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DEFAULT_SCHEMA</code>
     */
    public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();

    /**
     * The table <code>class_cache</code>.
     */
    public final ClassCache CLASS_CACHE = ClassCache.CLASS_CACHE;

    /**
     * The table <code>classified_ad</code>.
     */
    public final ClassifiedAd CLASSIFIED_AD = ClassifiedAd.CLASSIFIED_AD;

    /**
     * The table <code>event_data</code>.
     */
    public final EventData EVENT_DATA = EventData.EVENT_DATA;

    /**
     * The table <code>flyway_schema_history</code>.
     */
    public final FlywaySchemaHistory FLYWAY_SCHEMA_HISTORY = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY;

    /**
     * The table <code>user_profile</code>.
     */
    public final UserProfile USER_PROFILE = UserProfile.USER_PROFILE;

    /**
     * No further instances allowed
     */
    private DefaultSchema() {
        super("", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.<Table<?>>asList(
            ClassCache.CLASS_CACHE,
            ClassifiedAd.CLASSIFIED_AD,
            EventData.EVENT_DATA,
            FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY,
            UserProfile.USER_PROFILE);
    }
}
