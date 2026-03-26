CREATE TABLE tasks (
    id          BIGSERIAL    PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    status      VARCHAR(20)  NOT NULL DEFAULT 'TODO',
    priority    VARCHAR(20)  NOT NULL,
    due_date    DATE,
    project_id  BIGINT       NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL
);

CREATE TABLE task_tags (
    task_id BIGINT       NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
    tag     VARCHAR(100) NOT NULL
);

CREATE INDEX idx_tasks_project_id ON tasks(project_id);
CREATE INDEX idx_tasks_status     ON tasks(status);
CREATE INDEX idx_tasks_priority   ON tasks(priority);
