package com.marketplace.eventstore.impl.fixtures.classifiedad;

import com.google.common.eventbus.Subscribe;
import com.marketplace.eventstore.framework.event.EventListener;

@SuppressWarnings("UnstableApiUsage")
public class ClassifiedAdEventListener implements EventListener {
  private final ClassifiedAdEventProcessor eventProcessor;

  private final ClassifiedAdEventProcessor eventProcessor;

  public ClassifiedAdEventListener(ClassifiedAdEventProcessor eventProcessor) {
    this.eventProcessor = eventProcessor;
  }


  @Subscribe
  void on(ClassifiedAdCreated event) {
    var classifiedAd = eventProcessor.create(event);
    System.out.println(classifiedAd);
  }
}
