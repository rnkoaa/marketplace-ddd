create table event_data
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

CREATE
    INDEX idx_event_data_aggregate_id
    ON event_data (aggregate_id);

CREATE
    INDEX idx_event_data_stream_id
    ON event_data (stream_id);

CREATE
    INDEX idx_event_data_aggregate_name
    ON event_data (aggregate_name);

CREATE
    INDEX idx_event_data_stream_id_version
    ON event_data (stream_id, event_version);

create table user_profile
(
    id              TEXT PRIMARY KEY,
    first_name      TEXT NOT NULl,                 -- uuid
    last_name       TEXT NOT NULL,                 -- string
    middle_name     TEXT,                 -- string
    display_name    TEXT NOT NULl,                 -- json blob
    photos          TEXT,                 -- json blob
    created         TEXT        NOT NULL, -- timestamp
    updated         TEXT        NOT NULL  -- timestamp
);

CREATE
    UNIQUE INDEX idx_user_profile_displayName
    ON user_profile (display_name);

create table classified_ad
(
    id               TEXT PRIMARY KEY,
    approver         TEXT,
    owner            TEXT NOT NULL,
    title            TEXT NOT NULL,
    text             TEXT,
    status           TEXT NOT NULL,
    price            TEXT,
    pictures         TEXT,
    created          TEXT NOT NULL,
    updated          TEXT NOT NULL, -- timestamp,

    FOREIGN KEY (owner) REFERENCES user_profile (id)
);

CREATE
    INDEX idx_classified_ad_owner_id
    ON classified_ad (owner);

CREATE
    INDEX idx_classified_ad_approver_id
    ON classified_ad (approver);