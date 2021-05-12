package com.marketplace.context;

import com.marketplace.config.ApplicationConfig;
import dagger.Module;
import dagger.Provides;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.inject.Singleton;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

@Module
public abstract class JooqModule {
    @Provides
    @Singleton
    public static DSLContext provideDSLContext(ApplicationConfig applicationConfig) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(applicationConfig.getDb().getUrl());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (conn == null) {
            return null;
        }
        return DSL.using(conn, SQLDialect.SQLITE);
    }
}
