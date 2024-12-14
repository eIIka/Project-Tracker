package ua.ellka.model.user;

import lombok.Getter;
import lombok.Setter;
import ua.ellka.model.project.Project;
import ua.ellka.model.task.Task;
import ua.ellka.model.task.TaskStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class Employee extends User {
    Map<Project, Set<Task>> tasks = new HashMap<>();

    public Employee(Long id, String name, String email, String password, String phoneNumber,
                   LocalDateTime registeredAt, LocalDateTime lastLoginAt) {
        super(id, name, email, password, phoneNumber, registeredAt, lastLoginAt, UserRole.EMPLOYEE);
    }

    public void updateTaskStatus(Task task, TaskStatus status, String details) {
        boolean taskFound = tasks.values().stream()
                .flatMap(Set::stream)
                .anyMatch(t -> t.equals(task));
        if (!taskFound) {
            throw new IllegalArgumentException("Task is not assigned to this employee");
        }

        task.requestStatusChange(status, details);
    }

}
