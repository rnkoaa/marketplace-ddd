package com.marketplace;

import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class AbstractContainerInitializer {

  static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"))
      .withExposedPorts(27017);

  @BeforeAll
  static void setupAll() {
    mongoDBContainer.start();
  }
}
