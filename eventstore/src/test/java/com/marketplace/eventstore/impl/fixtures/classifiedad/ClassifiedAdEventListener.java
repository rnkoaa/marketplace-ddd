package com.marketplace.eventstore.impl.fixtures.classifiedad;

import com.marketplace.eventstore.framework.event.EventListener;

public class ClassifiedAdEventListener implements EventListener {
    private final EventProcessor eventProcessor;

    public ClassifiedAdEventListener(EventProcessor eventProcessor) {
        this.eventProcessor = eventProcessor;
    }
}
