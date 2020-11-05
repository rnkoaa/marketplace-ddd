package com.marketplace.context;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperModule {
    public static ObjectMapper objectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }
}
