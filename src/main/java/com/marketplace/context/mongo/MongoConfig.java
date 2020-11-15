package com.marketplace.context.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MongoConfig {
    String hosts;
    String database;
    int port;

    public String getConnectionString(){
        return String.format("mongodb://%s:%d", hosts, port);
    }
}
