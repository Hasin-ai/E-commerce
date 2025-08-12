-- PostgreSQL version of V12__create_processed_events_table.sql
CREATE TABLE processed_events (
    event_id VARCHAR(255) NOT NULL,
    processed_at TIMESTAMP NOT NULL,
    PRIMARY KEY (event_id)
);

