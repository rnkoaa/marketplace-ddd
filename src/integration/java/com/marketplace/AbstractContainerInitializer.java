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
  private static final String MONGO_IMAGE_TAG = "4.4";
  private static final String MONGO_IMAGE = String.format("mongo:%s", MONGO_IMAGE_TAG);
  static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse(MONGO_IMAGE))
      .withExposedPorts(27017);

  @BeforeAll
  static void setupAll() {
    mongoDBContainer.start();
  }


}
