package com.marketplace.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ClassifiedAdIdTest {
    String testUuid = "88dbae7a-4b21-48d4-a59e-57e42308d4b5";

    @Test
    void testClassifiedIdToString() {
        var classifiedAdId = ClassifiedAdId.fromString(testUuid);

        assertThat(classifiedAdId.toString()).isEqualTo(testUuid);
    }

    @Test
    void testUuidGenerated() {
        var classifiedAdId = ClassifiedAdId.fromString(testUuid);

        assertThat(classifiedAdId).isNotNull()
                .isEqualTo(new ClassifiedAdId(UUID.fromString(testUuid)));
    }

}