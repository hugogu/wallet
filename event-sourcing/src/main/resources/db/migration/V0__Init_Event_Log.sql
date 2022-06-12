CREATE TABLE IF NOT EXISTS command
(
    id           uuid PRIMARY KEY,
    command_type varchar(16) NOT NULL,
    command_data jsonb       NOT NULL,
    command_time timestamp   NOT NULL DEFAULT now(),
    create_time  timestamp   NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS event
(
    id           uuid PRIMARY KEY,
    command_id   uuid        NOT NULL,
    aggregate_id uuid        NOT NULL,
    event_type   varchar(16) NOT NULL,
    event_data   jsonb       NOT NULL,
    event_time   timestamp   NOT NULL DEFAULT now(),
    create_time  timestamp   NOT NULL DEFAULT now()
);
