CREATE TABLE IF NOT EXISTS account
(
    id           uuid PRIMARY KEY,
    account_type varchar(16) NOT NULL,
    number       varchar(32) NOT NULL,
    balance      decimal     NOT NULL,
    extra_info   jsonb       NULL,
    create_time  timestamp   NOT NULL DEFAULT now(),
    last_update  timestamp   NOT NULL DEFAULT now(),
    version      integer     NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS transaction
(
    id               uuid PRIMARY KEY,
    from_account     uuid      NOT NULL,
    to_account       uuid      NOT NULL,
    amount           decimal   NOT NULL,
    currency         char(3)   NOT NULL,
    transaction_time timestamp NOT NULL,
    settle_time      timestamp NOT NULL,
    create_time      timestamp NOT NULL DEFAULT now(),
    last_update      timestamp NOT NULL DEFAULT now(),
    version          integer   NOT NULL DEFAULT 0
);
