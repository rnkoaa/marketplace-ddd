package com.marketplace;

import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class AbstractContainerInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractContainerInitializer.class);

    @BeforeAll
    static void setupAll() {
    }


}
