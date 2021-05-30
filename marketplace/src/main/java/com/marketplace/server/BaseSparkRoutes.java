package com.marketplace.server;

import static com.marketplace.server.SparkServer.MEDIA_APPLICATION_JSON;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

public abstract class BaseSparkRoutes {

    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String NO_CONTENT = "";

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseSparkRoutes.class);

    private final ObjectMapper objectMapper;

    public BaseSparkRoutes(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected void setJsonHeaders(Response response) {
        response.header(HEADER_CONTENT_TYPE, MEDIA_APPLICATION_JSON);
        response.type(MEDIA_APPLICATION_JSON);
    }

    public <T> Try<T> deserialize(byte[] body, Class<T> clzz) {
        return Try.of(() -> objectMapper.readValue(body, clzz));
    }

    public <T> Try<T> deserialize(byte[] body, TypeReference<T> clzz) {
        return Try.of(() -> objectMapper.readValue(body, clzz));
    }

    public String getRequestParam(Request req, String param) {
        return req.params(":" + param);
    }

    public String serializeResponse(Object object) {
        return Try.of(() -> objectMapper.writeValueAsString(object))
            .onFailure(ex -> LOGGER.info("error while serializing object with message {}", ex.getMessage()))
            .getOrElse("");
    }


}
