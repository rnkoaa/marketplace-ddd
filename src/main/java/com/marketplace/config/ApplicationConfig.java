package com.marketplace.config;

import com.marketplace.context.mongo.MongoConfig;
import lombok.Data;

@Data
public class ApplicationConfig {
    private ServerConfig server = new ServerConfig();
    private MongoConfig mongoConfig = new MongoConfig();
}

