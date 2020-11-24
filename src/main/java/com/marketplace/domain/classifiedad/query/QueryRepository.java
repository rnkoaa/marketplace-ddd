package com.marketplace.domain.classifiedad.query;

import java.util.List;
import java.util.Optional;

public interface QueryRepository<T, U>{

  Optional<T> findById(U id);

  List<T> findAll();
}
