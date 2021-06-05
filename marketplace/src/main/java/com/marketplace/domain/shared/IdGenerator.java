package com.marketplace.domain.shared;

import java.util.UUID;

public interface IdGenerator {

    String EMPTY = "00000000-0000-0000-0000-000000000000";

    default UUID newUUID() {
        return UUID.randomUUID();
    }

}
