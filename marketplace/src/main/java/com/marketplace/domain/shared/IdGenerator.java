package com.marketplace.domain.shared;

import java.util.UUID;

public interface IdGenerator {

  default UUID newUUID() {
    return UUID.randomUUID();
  }

}
