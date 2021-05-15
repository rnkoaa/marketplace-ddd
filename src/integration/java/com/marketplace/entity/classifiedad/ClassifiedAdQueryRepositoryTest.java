package com.marketplace.entity.classifiedad;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.marketplace.BaseRepositoryTest;
import com.marketplace.domain.classifiedad.query.ClassifiedAdQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClassifiedAdQueryRepositoryTest extends BaseRepositoryTest {

    ClassifiedAdQueryRepository classifiedAdQueryRepository;

    @BeforeEach
    void setup() {
        classifiedAdQueryRepository = getApplicationContext().getClassifiedAdQueryRepository();
    }

    @Test
    void contextLoads() {
        assertThat(classifiedAdQueryRepository).isNotNull();
    }

}
