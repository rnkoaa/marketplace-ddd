package com.marketplace.eventstore.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.common.ObjectMapperBuilder;
import com.marketplace.cqrs.event.Event;
import com.marketplace.eventstore.framework.Result;
import com.marketplace.eventstore.jdbc.tables.records.EventDataRecord;
import java.util.List;

public class EventDataMapper {

    private static final ObjectMapper objectMapper = new ObjectMapperBuilder().build();
    private static EventClassCache eventClassCache = EventClassCache.getInstance();

    public static EventDataRecord map(Event event) {
        eventClassCache.put(event.getClass());
        return createEventRecord(event, serialize(event));
    }

    public static List<EventDataRecord> map(List<Event> events) {
        return events.stream()
            .peek(it -> eventClassCache.put(it.getClass()))
            .map(it -> createEventRecord(it, serialize(it)))
            .toList();
    }

    private static EventDataRecord createEventRecord(Event event, Result<String> mappedEvent) {
        return new EventDataRecord(
            0,
            event.getId().toString(),
            event.getAggregateName(),
            event.getAggregateId().toString(),
            event.getClass().getSimpleName(),
            (int) event.getVersion(),
            mappedEvent.orElse(""),
            event.getCreatedAt().toString()
        );
    }

    public static Result<Event> deserialize(EventDataRecord eventDataRecord) {
        String eventType = eventDataRecord.getEventType();
        Result<Class<?>> classResult = eventClassCache.get(eventType);
        return classResult
            .flatmap(clzz -> deserialize(eventDataRecord.getData(), clzz));
    }

    private static Result<Event> deserialize(String data, Class<?> clzz) {

        try {
            Object o = objectMapper.readValue(data, clzz);
            return Result.ofNullable((Event) o);
        } catch (JsonProcessingException ex) {
            return Result.error(ex);
        }
    }

    private static Result<String> serialize(Event event) {

        try {
            String res = objectMapper.writeValueAsString(event);
            return Result.of(res);
        } catch (JsonProcessingException e) {
            return Result.error(e);
        }
    }

}
