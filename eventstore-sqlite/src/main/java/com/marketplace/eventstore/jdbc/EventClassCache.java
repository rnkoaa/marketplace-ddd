package com.marketplace.eventstore.jdbc;

import com.marketplace.cqrs.event.Event;
import com.marketplace.eventstore.framework.Result;
import java.util.HashMap;
import java.util.Map;

public class EventClassCache {

    private final Map<String, Class<?>> eventCache = new HashMap<>();
    private static final EventClassCache instance = new EventClassCache();

    private EventClassCache() {
    }

    public static EventClassCache getInstance() {
        return instance;
    }

    public void put(Class<?> eventClass) {
        eventCache.put(eventClass.getSimpleName(), eventClass);
    }

    public Result<Class<?>> get(String className) {
        Class<?> eventClass = eventCache.get(className);
        return Result.ofNullable(eventClass);
    }

}
