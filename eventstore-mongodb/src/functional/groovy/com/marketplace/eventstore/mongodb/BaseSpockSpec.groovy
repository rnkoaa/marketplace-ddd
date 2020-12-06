package com.marketplace.eventstore.mongodb

import com.fasterxml.jackson.databind.ObjectMapper
import com.marketplace.common.ObjectMapperBuilder
import groovy.transform.PackageScope
import spock.lang.Shared
import spock.lang.Specification

class BaseSpockSpec extends Specification {
    @Shared
    ObjectMapper objectMapper

    def setupSpec() {
        objectMapper = new ObjectMapperBuilder()
                .build()
    }

    String serialize(Object object) {
        return objectMapper.writeValueAsString(object)
    }

    @PackageScope
    <T> T deserialize(String value, Class<T> clzz) {
        return objectMapper.readValue(value, clzz)
    }
}
