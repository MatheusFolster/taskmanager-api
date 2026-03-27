CREATE TABLE subtasks (
    id         BIGSERIAL    PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    completed  BOOLEAN      NOT NULL DEFAULT FALSE,
    task_id    BIGINT       NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
    created_at TIMESTAMP    NOT NULL
);
