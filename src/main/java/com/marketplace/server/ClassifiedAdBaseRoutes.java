package com.marketplace.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

public abstract class ClassifiedAdBaseRoutes {
  private static final Logger LOGGER = LoggerFactory.getLogger(ClassifiedAdBaseRoutes.class);
    protected ObjectMapper objectMapper;

   protected ClassifiedAdBaseRoutes(ObjectMapper objectMapper){
     this.objectMapper = objectMapper;
   }

    protected String getClassifiedIdFromRequest(Request req) {
        return req.params(":classifiedAdId");
    }

    public String serializeResponse(Object object) {
        return Try.of(() -> objectMapper.writeValueAsString(object))
            .onFailure(ex -> LOGGER.info("error while serialing object with message {}", ex.getMessage()))
            .getOrElse("");
    }
}
