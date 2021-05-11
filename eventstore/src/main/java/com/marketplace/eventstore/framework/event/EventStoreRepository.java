package com.marketplace.eventstore.framework.event;

import com.marketplace.cqrs.event.Event;
import com.marketplace.eventstore.framework.Result;
import java.util.List;

public interface EventStoreRepository<T, U> {

    /**
     * Load all events for an aggregate using {@param aggregateName}
     *
     * @param aggregateName the name of the aggregate that is being loaded
     * @param fromVersion   the beginning version of events. This allows loading a subset of events if an aggregate has
     *                      lots of events
     * @return list of events for the aggregate.
     */
    List<Event> load(String aggregateName, int fromVersion);

    /**
     * Load all events for an aggregate using {@param aggregateId}
     *
     * @param aggregateId the id of the aggregate that is being loaded
     * @param fromVersion the beginning version of events. This allows loading a subset of events if an aggregate has
     *                    lots of events
     * @return list of events for the aggregate.
     */
    List<Event> load(U aggregateId, int fromVersion);

    /**
     * Load all events for an aggregate using {@param aggregateId}
     *
     * @param aggregateId the id of the aggregate that is being loaded
     * @return list of events for the aggregate.
     */
    List<Event> load(U aggregateId);

    /**
     * Load all events of an aggregate using the {@param aggregateName}
     *
     * @param aggregateName the name of the aggregate to load
     * @return all events related to the aggregate
     */
    List<Event> load(String aggregateName);

    /**
     * Save an event for an aggregate
     *
     * @param aggregateId the id of the aggregate to save
     * @param event       The event being saved for the aggregate
     * @return the number of rows affected.
     */
    Result<Integer> save(U aggregateId, T event);

    /**
     * persist multiple events for an aggregate
     *
     * @param event           list of events being persisted for an aggregate.
     *                        event.aggregateName} is not null
     * @return internal id of the row persisted.
     */
    Result<Integer> save(T event);

    /**
     * persist multiple events for an aggregate
     *
     * @param aggregateId     the id of the aggregate
     * @param events           list of events being persisted for an aggregate.
     *                        event.aggregateName} is not null
     * @param expectedVersion the expected current version of the aggregate
     * @return internal id of the row persisted.
     */
    Result<Integer> save(U aggregateId, List<T> events, int expectedVersion);

    /**
     * save an event for
     *
     * @param aggregateId     the id of the aggregate
     * @param event           being persisted. Ensure that the {@param event.aggregateId} is not null as well as {@param
     *                        event.aggregateName} is not null
     * @param expectedVersion the expected current version of the aggregate
     * @return internal id of the row persisted.
     */
    Result<Integer> save(U aggregateId, T event, int expectedVersion);

    /**
     * save an event for
     *
     * @param event           being persisted. Ensure that the {@param event.aggregateId} is not null as well as {@param
     *                        event.aggregateName} is not null
     * @param expectedVersion the expected current version of the aggregate
     * @return internal id of the row persisted.
     */
    Result<Integer> save(T event, int expectedVersion);

    /**
     * Provide the latest version of the aggregate.
     *
     * @param aggregateId id of the aggregate
     * @return latest or maximum version of the aggregate
     */
    Integer getVersion(U aggregateId);

    /**
     * Provides the number of events for an aggregate
     *
     * @param aggregateId the Id of the aggregate to find number of events for
     * @return the number of events persisted for the aggregate
     */
    Long countEvents(U aggregateId);
}
