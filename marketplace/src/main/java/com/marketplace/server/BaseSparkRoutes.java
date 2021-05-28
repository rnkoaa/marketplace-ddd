package com.marketplace.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseSparkRoutes {

    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String NO_CONTENT = "";

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseSparkRoutes.class);

    private final ObjectMapper objectMapper;

    public BaseSparkRoutes(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> Try<T> deserialize(byte[] body, Class<T> clzz) {
        return Try.of(() -> objectMapper.readValue(body, clzz));
    }

    public String serializeResponse(Object object) {
        return Try.of(() -> objectMapper.writeValueAsString(object))
            .onFailure(ex -> LOGGER.info("error while serializing object with message {}", ex.getMessage()))
            .getOrElse("");
    }


}
