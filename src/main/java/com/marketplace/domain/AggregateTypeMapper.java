package com.marketplace.domain;

import com.marketplace.cqrs.event.EventId;
import com.marketplace.cqrs.event.VersionedEvent;
import com.marketplace.cqrs.framework.AggregateRoot;
import io.vavr.control.Try;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AggregateTypeMapper {

    private final static AggregateTypeMapper instance = new AggregateTypeMapper();
    private final Map<String, String> instanceInfo = new HashMap<>();

    private AggregateTypeMapper() {
    }

    public static AggregateTypeMapper getInstance() {
        return instance;
    }

    public void put(AggregateRoot<EventId, VersionedEvent> aggregateRoot) {
        instanceInfo.put(aggregateRoot.getClass().getSimpleName(), aggregateRoot.getClass().getName());
    }

    public Optional<String> getClassName(String name) {
        return Optional.ofNullable(instanceInfo.get(name));
    }

    public Try<Class<?>> getClassInfo(String name) {
        String className = instanceInfo.get(name);
        if (className == null || className.isEmpty()) {
            return Try.failure(new IllegalArgumentException("class with name was not found"));
        }
        return Try.of(() -> Class.forName(className));
    }
}
