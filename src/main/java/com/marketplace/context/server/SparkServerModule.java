package com.marketplace.context.server;

import com.marketplace.config.ApplicationConfig;
import com.marketplace.config.ServerConfig;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public abstract class SparkServerModule {
    static int DEFAULT_PORT = 8080;

    @Named("server.port")
    @Provides
    @Singleton
    public static int provideServerPort(ApplicationConfig applicationConfig) {
        ServerConfig server = applicationConfig.getServer();
        if (server == null) {
            return DEFAULT_PORT;
        }
        if (server.getPort() <= 0) {
            return DEFAULT_PORT;
        }
        return server.getPort();
    }
}
