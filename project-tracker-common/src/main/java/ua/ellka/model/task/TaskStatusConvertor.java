package ua.ellka.model.task;

import jakarta.persistence.AttributeConverter;

public class TaskStatusConvertor implements AttributeConverter<TaskStatus, String> {


    @Override
    public String convertToDatabaseColumn(TaskStatus status) {
        return status == null ? null : status.getStatus();
    }

    @Override
    public TaskStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return TaskStatus.NOT_STARTED;
        }

        return TaskStatus.fromString(dbData);
    }
}

