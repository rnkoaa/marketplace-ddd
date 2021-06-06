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
import org.sqlite.SQLiteConfig;

@Module
public abstract class JooqModule {

    @Provides
    @Singleton
    public static DSLContext provideDSLContext(ApplicationConfig applicationConfig) {
        Connection conn = null;
        try {
//            SQLiteConfig config = new SQLiteConfig();
//            config.setSharedCache(true);
//            config.enableLoadExtension(true);
//            config.enforceForeignKeys(true);
            //displayName
            conn = DriverManager.getConnection(applicationConfig.getDb().getDbUrl());

            // we always need a foreign key support
//            enableForeignKeySupport(conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (conn == null) {
            return null;
        }
        //
        return DSL.using(conn, SQLDialect.SQLITE);
    }

    private static void enableForeignKeySupport(Connection conn) throws SQLException {
        var stmt = conn.createStatement();
        String sql = "PRAGMA foreign_keys = ON;";
        stmt.execute(sql);
        stmt.close();
    }
}
