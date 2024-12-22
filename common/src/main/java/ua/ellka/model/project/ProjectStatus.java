package ua.ellka.model.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectStatus {
    PLANNED ("Planned"),
    IN_PROGRESS ("In Progress"),
    COMPLETED ("Completed"),
    ON_HOLD ("On Hold"),
    ARCHIVED ("Archived");

    private final String status;

    public static ProjectStatus fromString(String status) {
        for (ProjectStatus projectStatus : ProjectStatus.values()) {
            if (projectStatus.getStatus().equals(status)) {
                return projectStatus;
            }
        }
        return ProjectStatus.PLANNED;
    }
}
