CREATE TABLE event_entity (
    id UUID PRIMARY KEY,
    event_type VARCHAR(255),
    user_id VARCHAR(255),
    session_id VARCHAR(255),
    details jsonb,
    timestamp TIMESTAMP
);
