create table event_record (
    id INTEGER PRIMARY KEY,
    event_id TEXT NOT NULL,
    aggregate_id TEXT NOT NULL,
    event_type TEXT NOT NULL,
    version integer,
    data TEXT NOT NULL,
    created TEXt NOT NULL
);
