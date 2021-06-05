package com.marketplace.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ConfigModule {
    private static ObjectMapper instance;

    private ConfigModule() {

    }


    public static ObjectMapper objectMapper() {
        if (instance == null) {
            synchronized (ConfigModule.class) {
                instance = new ObjectMapper(new YAMLFactory()).findAndRegisterModules();
                instance.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());

                // Ignore null values when writing json.
                instance.setSerializationInclusion(JsonInclude.Include.NON_NULL);

                // Write times as a String instead of a Long so its human readable.
                instance.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
                instance.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE)
                instance.registerModule(new JavaTimeModule());
            }
        }
        return instance;
    }
}
