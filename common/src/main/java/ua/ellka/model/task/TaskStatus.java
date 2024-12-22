package ua.ellka.model.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskStatus {
    NOT_STARTED ("Not Started"),
    TODO ("TODO"),
    IN_PROGRESS ("In Progress"),
    NEEDS_REVIEW ("Needs Review"),
    BLOCKED ("Blocked"),
    COMPLETED ("Completed"),;

    private final String status;

    public static TaskStatus fromStatus(String status) {
        for (TaskStatus taskStatus : TaskStatus.values()) {
            if (taskStatus.getStatus().equalsIgnoreCase(status)) {
                return taskStatus;
            }
        }
        return TaskStatus.NOT_STARTED;
    }
}
