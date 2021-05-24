package com.marketplace.domain.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class RedisConverter {
    private final ObjectMapper objectMapper;

    public RedisConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String serialize(Object entity) {
        try {
            return objectMapper.writeValueAsString(entity);
        } catch (JsonProcessingException ignored) {
        }
        return "";
    }

    public <T> Optional<T> deserialize(String object, Class<T> clzz) {
        try {
            T res = objectMapper.readValue(object, clzz);
            return Optional.ofNullable(res);
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }
}
