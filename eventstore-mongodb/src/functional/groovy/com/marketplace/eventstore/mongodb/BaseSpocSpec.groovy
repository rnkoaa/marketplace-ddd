package com.marketplace.eventstore.mongodb

import com.fasterxml.jackson.databind.ObjectMapper
import com.marketplace.common.ObjectMapperBuilder
import groovy.transform.PackageScope
import spock.lang.Specification

class BaseSpocSpec extends Specification {
    ObjectMapper objectMapper

    def setup() {
        objectMapper = new ObjectMapperBuilder()
                .build()
    }

    /**
     * Getter is required for intellij to give you the declaration
     *
     * @return object objectMapper
     */
    ObjectMapper getObjectMapper() {
        return objectMapper
    }

    String serialize(Object object) {
        return objectMapper.writeValueAsString(object)
    }

    @PackageScope
    <T> T deserialize(String value, Class<T> clzz) {
        return objectMapper.readValue(value, clzz)
    }
}
