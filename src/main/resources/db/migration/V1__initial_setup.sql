create table event_data
(
    id             TEXT PRIMARY KEY,
    event_id       TEXT NOT NULL,
    aggregate_name TEXT NULL,
    aggregate_id   TEXT NOT NULL,
    event_type     TEXT NOT NULL,
    event_version  integer,
    data           TEXT NOT NULL,
    created        TEXt NOT NULL
);

-- [jooq ignore start]
CREATE
    UNIQUE INDEX idx_event_data_event_id
    ON event_data (event_id);

CREATE
    INDEX idx_event_data_aggregate_id
    ON event_data (aggregate_id);

CREATE
    INDEX idx_event_data_aggregate_name
    ON event_data (aggregate_name);
-- [jooq ignore end]

create table user_profile
(
    id              TEXT PRIMARY KEY,

    -- [jooq ignore start]
    -- uuid
    -- [jooq ignore end]
    user_profile_id TEXT NOT NULL,
    firstName       TEXT,                 -- uuid
    lastName        TEXT,                 -- string
    middleName      TEXT,                 -- string
    displayName     TEXT,                 -- json blob
    photos          TEXT,                 -- json blob
    created         TEXT        NOT NULL, -- timestamp
    updated         TEXT        NOT NULL  -- timestamp
);

-- [jooq ignore start]
CREATE
    INDEX idx_user_profile_id
    ON user_profile (user_profile_id);

-- [jooq ignore end]


create table classified_ad
(
    id               TEXT PRIMARY KEY,

    -- [jooq ignore start]
    -- uuid
    -- [jooq ignore end]
    classified_ad_id TEXT NOT NULL,

    -- [jooq ignore start]
    -- uuid
    -- [jooq ignore end]
    approver         TEXT NOT NULL,

    -- [jooq ignore start]
    -- uuid
    -- [jooq ignore end]
    owner            TEXT NOT NULL,

    title            TEXT NOT NULL,
    text             TEXT,

    -- [jooq ignore start]
    -- json blog
    -- [jooq ignore end]
    price            TEXT,

    -- [jooq ignore start]
    -- json blog
    -- [jooq ignore end]
    pictures         TEXT,
    created          TEXT NOT NULL,
    updated          TEXT NOT NULL, -- timestamp,

    FOREIGN KEY (owner) REFERENCES user_profile (user_profile_id)
);

-- [jooq ignore start]
CREATE
    INDEX idx_classified_ad_id
    ON classified_ad (classified_ad_id);

CREATE
    INDEX idx_classified_ad_owner_id
    ON classified_ad (owner);

CREATE
    INDEX idx_classified_ad_approver_id
    ON classified_ad (approver);
-- [jooq ignore end]