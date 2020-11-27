package com.marketplace.eventstore.impl.fixtures.classifiedad;

import com.marketplace.eventstore.impl.fixtures.classifiedad.query.ClassifiedAdQueryModel;

public class ClassifiedAdEventProcessor implements EventProcessor {

  public ClassifiedAdQueryModel create(ClassifiedAdCreated event) {
    return new ClassifiedAdQueryModel(event.aggregateId(), event.getOwner());
  }
}
