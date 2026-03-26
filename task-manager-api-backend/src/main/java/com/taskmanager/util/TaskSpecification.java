package com.taskmanager.util;

import com.taskmanager.entity.Project;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.enums.TaskPriority;
import com.taskmanager.entity.enums.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

public final class TaskSpecification {

    private TaskSpecification() {
    }

    public static Specification<Task> belongsToProject(Project project) {
        return (root, query, cb) -> cb.equal(root.get("project"), project);
    }

    public static Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, cb) ->
                status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    public static Specification<Task> hasPriority(TaskPriority priority) {
        return (root, query, cb) ->
                priority == null ? cb.conjunction() : cb.equal(root.get("priority"), priority);
    }

    public static Specification<Task> hasTag(String tag) {
        return (root, query, cb) -> {
            if (tag == null) return cb.conjunction();
            if (query != null && !Long.class.equals(query.getResultType())) {
                query.distinct(true);
            }
            var tagsJoin = root.join("tags");
            return cb.equal(tagsJoin, tag);
        };
    }
}
