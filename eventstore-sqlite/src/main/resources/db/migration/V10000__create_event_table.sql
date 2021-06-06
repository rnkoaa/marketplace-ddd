create table if not exists event_data
(
    id             TEXT PRIMARY KEY,
    stream_id      TEXT NOT NULL,
    aggregate_name TEXT NOT NULL,
    aggregate_id   TEXT NOT NULL,
    event_type     TEXT NOT NULL,
    event_version  integer,
    data           TEXT NOT NULL,
    created        TEXt NOT NULL,
    check(event_version > 0),
    check(length(id) > 0)
);

-- [jooq ignore start]
CREATE
    INDEX if not exists idx_event_data_aggregate_id
    ON event_data (aggregate_id);

CREATE
    INDEX if not exists idx_event_data_stream_id
    ON event_data (stream_id);

CREATE
    INDEX if not exists idx_event_data_aggregate_name
    ON event_data (aggregate_name);

CREATE
    INDEX if not exists idx_event_data_stream_id_version
    ON event_data (stream_id, event_version);
-- [jooq ignore end]