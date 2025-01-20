package ua.ellka.model.project;

import jakarta.persistence.AttributeConverter;
import ua.ellka.model.task.TaskStatus;

public class ProjectStatusConvertor implements AttributeConverter<ProjectStatus, String> {

    @Override
    public String convertToDatabaseColumn(ProjectStatus status) {
        return status == null ? null : status.getStatus();
    }

    @Override
    public ProjectStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return ProjectStatus.PLANNED;
        }

        return ProjectStatus.fromString(dbData);
    }

}
