package com.marketplace.domain;

import com.marketplace.domain.classifiedad.ClassifiedAdId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ClassifiedAdIdTest {
    String testUuid = "88dbae7a-4b21-48d4-a59e-57e42308d4b5";

    @Test
    void testClassifiedIdToString() {
        var classifiedAdId = ClassifiedAdId.from(testUuid);

        assertThat(classifiedAdId.toString()).isEqualTo(testUuid);
    }


    @Test
    void classifiedAdIdFromUUIDIsValid() {
        assertThat(ClassifiedAdId.newClassifiedAdId().isValid()).isTrue();
    }

    @Test
    void emptyClassifiedAdIdIsInvalid() {
        assertThat(ClassifiedAdId.EMPTY_VALUE.isValid()).isFalse();
    }


    @Test
    void testUuidGenerated() {
        var classifiedAdId = ClassifiedAdId.from(testUuid);

        assertThat(classifiedAdId).isNotNull()
                .isEqualTo(new ClassifiedAdId(UUID.fromString(testUuid)));
    }

}