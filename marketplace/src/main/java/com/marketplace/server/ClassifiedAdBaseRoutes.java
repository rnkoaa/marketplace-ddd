package com.marketplace.server;

import spark.Request;

public abstract class ClassifiedAdBaseRoutes {

    protected String getClassifiedIdFromRequest(Request req) {
        return req.params(":classifiedAdId");
    }

}
