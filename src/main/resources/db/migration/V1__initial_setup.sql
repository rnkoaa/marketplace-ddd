create table event_data
(
    id             TEXT PRIMARY KEY,
    aggregate_name TEXT NULL,
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
    INDEX idx_event_data_aggregate_name
    ON event_data (aggregate_name);

CREATE
    INDEX idx_event_data_aggregate_name_version
    ON event_data (aggregate_name, event_version);

create table user_profile
(
    id              TEXT PRIMARY KEY,
    firstName       TEXT,                 -- uuid
    lastName        TEXT,                 -- string
    middleName      TEXT,                 -- string
    displayName     TEXT,                 -- json blob
    photos          TEXT,                 -- json blob
    created         TEXT        NOT NULL, -- timestamp
    updated         TEXT        NOT NULL  -- timestamp
);

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