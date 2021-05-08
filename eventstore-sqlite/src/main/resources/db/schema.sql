create table event_data (
    id INTEGER PRIMARY KEY,
    event_id TEXT NOT NULL,
    aggregate_name TEXT NULL,
    aggregate_id TEXT NOT NULL,
    event_type TEXT NOT NULL,
    version integer,
    data TEXT NOT NULL,
    created TEXt NOT NULL
);

CREATE UNIQUE INDEX idx_event_data_event_id
ON event_data (event_id);

CREATE INDEX idx_event_data_aggregate_id
ON event_data(aggregate_id);

CREATE INDEX idx_event_data_aggregate_name
ON event_data(aggregate_name);
