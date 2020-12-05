package com.marketplace.config;

import com.marketplace.common.config.MongoConfig;
import lombok.Data;

@Data
public class ApplicationConfig {
    private ServerConfig server = new ServerConfig();
    private MongoConfig mongo = new MongoConfig();
}

