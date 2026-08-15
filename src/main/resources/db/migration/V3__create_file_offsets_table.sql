CREATE TABLE if not exists file_offsets (
     id   BIGSERIAL    PRIMARY KEY,
    file_path VARCHAR(500) NOT NULL UNIQUE,
    byte_offset BIGINT  NOT NULL DEFAULT 0,
    last_read_at TIMESTAMP default now()
);