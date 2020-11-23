package com.marketplace.domain.classifiedad.read;

import java.util.Optional;
import java.util.UUID;

public interface ReadRepository<T, U>{

  Optional<T> findById(U id);
}
