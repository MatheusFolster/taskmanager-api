CREATE TABLE refresh_tokens (
    id          BIGSERIAL    PRIMARY KEY,
    token       VARCHAR(255) NOT NULL UNIQUE,
    user_id     BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL
);
