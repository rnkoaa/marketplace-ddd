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
