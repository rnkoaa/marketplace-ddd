create table event_data
(
--    id             VARCHAR(40) PRIMARY KEY,
    id             TEXT PRIMARY KEY,
    aggregate_name TEXT NULL,
    aggregate_id   TEXT NOT NULL,
    event_type     TEXT NOT NULL,
    event_version  integer,
    data           TEXT NOT NULL,
    created        TEXt NOT NULL
);

-- [jooq ignore start]
CREATE
    INDEX idx_event_data_aggregate_id
    ON event_data (aggregate_id);

CREATE
    INDEX idx_event_data_aggregate_name
    ON event_data (aggregate_name);

CREATE
    INDEX idx_event_data_aggregate_name
    ON event_data (aggregate_name);
-- [jooq ignore end]