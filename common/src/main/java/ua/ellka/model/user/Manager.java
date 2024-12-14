package ua.ellka.model.user;

import lombok.Getter;
import lombok.Setter;
import ua.ellka.model.project.Project;
import ua.ellka.model.project.ProjectStatus;
import ua.ellka.model.task.Task;
import ua.ellka.model.task.TaskStatus;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
public class Manager extends User {
    Set<Project> projects = new HashSet<>();

    public Manager(Long id, String name, String email, String password, String phoneNumber,
                   LocalDateTime registeredAt, LocalDateTime lastLoginAt) {
        super(id, name, email, password, phoneNumber, registeredAt, lastLoginAt, UserRole.MANAGER);
    }


    public void addProject(Project project) {
        validateRole(UserRole.MANAGER);
        projects.add(project);
    }

    public void removeProject(Project project) {
        validateRole(UserRole.MANAGER);
        projects.remove(project);
    }

    public void manageProject(Project project, ProjectStatus status) {
        validateRole(UserRole.MANAGER);
        project.setStatus(status);
    }

    public Set<Employee> getEmployeesInProject(Project project) {
        validateRole(UserRole.MANAGER);
        validateProject(project);
        return new HashSet<>(project.getEmployees());
    }

    public Map<Task, TaskStatus> getTasksInProject(Project project) {
        validateRole(UserRole.MANAGER);
        validateProject(project);

        return project.getTasks().stream()
                .collect(Collectors.toMap(task -> task, Task::getStatus));
    }

    public void addTask(Project project, Task task) {
        validateRole(UserRole.MANAGER);
        validateProject(project);

        if (project.getTasks().contains(task)) {
            throw new IllegalArgumentException("Task already exists");
        }

        project.getTasks().add(task);
    }

    public void removeTask(Project project, Task task) {
        validateRole(UserRole.MANAGER);
        validateTaskInProject(project, task);

        project.getTasks().remove(task);
    }

    public void addTaskForEmployee(Project project, Task task, Employee employee) {
        validateRole(UserRole.MANAGER);
        validateTaskInProject(project, task);
        validateEmployeeInProject(project, employee);

        if (task.getEmployee() != null) {
            throw new IllegalArgumentException("Task already exists");
        }

        employee.getTasks().computeIfAbsent(project, v -> new HashSet<>()).add(task);
        task.setEmployee(employee);

        if (task.getStatus() == null || task.getStatus() == TaskStatus.TODO) {
            task.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public void removeTaskForEmployee(Project project, Task task, Employee employee) {
        validateRole(UserRole.MANAGER);
        validateTaskInProject(project, task);
        validateEmployeeInProject(project, employee);

        employee.getTasks().remove(project, task);
        task.setEmployee(null);
        task.setStatus(TaskStatus.TODO);

    }

    public void approveTaskStatus(Task task) {
        validateRole(UserRole.MANAGER);
        if (task.getPendingStatus() == null) {
            throw new IllegalArgumentException("No pending status change request for this task");
        }

        task.approveStatusChange();
    }

    public void rejectTaskStatus(Task task) {
        validateRole(UserRole.MANAGER);
        if (task.getPendingStatus() == null) {
            throw new IllegalArgumentException("No pending status change request for this task");
        }
        task.rejectStatusChange();
    }

    private void validateProject(Project project) {
        if (!projects.contains(project)) {
            throw new IllegalArgumentException("Project not in projects");
        }
    }

    private void validateTaskInProject(Project project, Task task) {
        validateProject(project);
        if (!project.getTasks().contains(task)) {
            throw new IllegalArgumentException("Task not associated with the project");
        }
    }

    private void validateEmployeeInProject(Project project, Employee employee) {
        validateProject(project);
        if (!project.getEmployees().contains(employee)) {
            throw new IllegalArgumentException("Employee not associated with the project");
        }
    }

}
