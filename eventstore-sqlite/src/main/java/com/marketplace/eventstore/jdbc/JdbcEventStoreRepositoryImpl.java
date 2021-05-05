package com.marketplace.eventstore.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.common.ObjectMapperBuilder;
import com.marketplace.cqrs.event.Event;
import com.marketplace.eventstore.framework.event.EventRecord;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class JdbcEventStoreRepositoryImpl implements JdbcEventStoreRepository {

    Map<String, Class<?>> eventClassCache = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapperBuilder().build();
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcEventStoreRepositoryImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public JdbcEventStoreRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mono<List<Event>> load(UUID aggregateId, int fromVersion) {
        String query = "select * from event where aggregate_id = ? and version >= ?";
        List<Object> params = List.of(aggregateId, fromVersion);
        List<Event> events = new ArrayList<>();
        try {
            ResultSet resultSet = jdbcTemplate.executeFindQuery(query, params);
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String eventType = resultSet.getString("event_type");
                long version = resultSet.getLong("version");
                String eventStr = resultSet.getString("event");
//                String  = resultSet.getString("aggregate_id");
                Class<?> clzz = eventClassCache.get(eventType);
                Event event = (Event) objectMapper.readValue(eventStr, clzz);
                events.add(event);
            }
        } catch (SQLException | JsonProcessingException ex) {
            LOGGER.debug("exception reading data {0}", ex);
        }
        return Mono.just(events);
    }

    @Override
    public Mono<List<Event>> load(UUID aggregateId) {
        return null;
    }

    @Override
    public Mono<Optional<Boolean>> save(UUID aggregateId, Event event) {
        return null;
    }

    @Override
    public Mono<Optional<Boolean>> save(Event event) {
        return null;
    }

    @Override
    public Mono<Optional<Boolean>> save(UUID aggregateId, List<Event> events, int version) {
        return null;
    }

    @Override
    public Mono<Optional<Boolean>> save(UUID aggregateId, Event event, int version) {
        return null;
    }

    @Override
    public Mono<Optional<Boolean>> save(Event event, int version) {
        return null;
    }

    @Override
    public Mono<Integer> getVersion(UUID aggregateId) {
        return null;
    }

    @Override
    public Mono<Long> countEvents(UUID aggregateId) {
        return null;
    }
}
