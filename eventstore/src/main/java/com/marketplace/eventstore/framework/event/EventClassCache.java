package com.marketplace.eventstore.framework.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EventClassCache {
  private final Map<String, Class<?>> cache = new HashMap<>();
  private static EventClassCache eventClassCache;

  private EventClassCache() {}

  public static EventClassCache getInstance() {
    if (eventClassCache == null) {
      eventClassCache = new EventClassCache();
    }
    return eventClassCache;
  }

  public Optional<Class<?>> get(String className) {
    Class<?> aClass = cache.get(className);
    if (aClass != null) {
      return Optional.of(aClass);
    }

    try {
      Class<?> clzz = Class.forName(className);
      cache.put(className, clzz);
      return Optional.of(clzz);
    } catch (ClassNotFoundException e) {
      return Optional.empty();
    }
  }
}
