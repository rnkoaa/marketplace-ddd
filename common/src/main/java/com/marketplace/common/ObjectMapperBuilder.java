package com.marketplace.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ObjectMapperBuilder {

  public ObjectMapper build() {
    var objectMapper = new ObjectMapper().findAndRegisterModules();
    objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());

    // Ignore null values when writing json.
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    // Write times as a String instead of a Long so its human readable.
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE)
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

}
