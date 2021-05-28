package com.marketplace.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;

public abstract class ClassifiedAdBaseRoutes extends BaseSparkRoutes {

    public ClassifiedAdBaseRoutes(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    protected String getClassifiedIdFromRequest(Request req) {
        return req.params(":classifiedAdId");
    }

}
