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

    public static TaskStatus fromString(String status) {
        for (TaskStatus taskStatus : TaskStatus.values()) {
            if (taskStatus.getStatus().equalsIgnoreCase(status)) {
                return taskStatus;
            }
        }
        throw new IllegalArgumentException("No enum constant for task status: " + status);
    }
}
