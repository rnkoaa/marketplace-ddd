package com.marketplace.eventstore.jdbc;

import static com.marketplace.eventstore.jdbc.Tables.CLASS_CACHE;

import com.marketplace.eventstore.jdbc.tables.records.ClassCacheRecord;
import io.vavr.control.Try;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;

public class EventClassCache {

    private final Map<String, Class<?>> eventCache = new HashMap<>();
    private static EventClassCache instance;
    private final DSLContext dslContext;

    private EventClassCache(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public static EventClassCache getInstance(DSLContext dslContext) {
        if (instance == null) {
            instance = new EventClassCache(dslContext);
        }
        return instance;
    }

    public void put(Class<?> eventClass) {
        eventCache.put(eventClass.getSimpleName(), eventClass);
        ClassCacheRecord classCacheRecord = new ClassCacheRecord(eventClass.getSimpleName(), eventClass.getName());
        try {
            dslContext.insertInto(CLASS_CACHE)
                .set(classCacheRecord)
                .execute();
        } catch (DataAccessException ignored) {

        }
    }

    public Try<? extends Class<?>> get(String className) {
        Class<?> eventClass = eventCache.get(className);
        if (eventClass == null) {
            Optional<ClassCacheRecord> classCacheRecord = dslContext.select()
                .from(CLASS_CACHE)
                .where(CLASS_CACHE.ID.eq(className))
                .fetchOptionalInto(ClassCacheRecord.class);
            return classCacheRecord
                .map(clzz -> Try.of(() -> Class.forName(clzz.getClassName())))
                .orElseGet(() -> Try.failure(new RuntimeException("Class '" + className + "' not found")));
        }
        return Try.of(() -> eventClass);
    }

}
