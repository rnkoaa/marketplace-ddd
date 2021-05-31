package com.marketplace.server;

import static com.marketplace.server.SparkServer.MEDIA_APPLICATION_JSON;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.cqrs.event.VersionedEvent;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.eventstore.framework.event.EventStream;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import spark.Spark;

@Named
@Singleton
public class EventSparkRoutes extends BaseSparkRoutes {

    private final EventStore<VersionedEvent> eventStore;

    @Inject
    public EventSparkRoutes(ObjectMapper objectMapper, EventStore<VersionedEvent> eventStore) {
        super(objectMapper);
        this.eventStore = eventStore;
    }

    public void register(String baseEndpoint) {
        // load all events for an aggregate
        Spark.get(String.format("%s/:stream_id", baseEndpoint), MEDIA_APPLICATION_JSON, ((request, response) -> {
            setJsonHeaders(response);
            String streamId = request.params("stream_id");
            EventStream<VersionedEvent> load = eventStore.load(streamId);
            return serializeResponse(load);
        }));

        // count events
        Spark.get(String.format("%s/:stream_id/count", baseEndpoint), MEDIA_APPLICATION_JSON, ((request, response) -> {
            setJsonHeaders(response);
            String streamId = request.params("stream_id");
            return serializeResponse(
                Map.of(
                    "count", eventStore.load(streamId).size()
                ));
        }));
    }

}
