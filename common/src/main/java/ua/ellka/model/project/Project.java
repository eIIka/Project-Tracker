package ua.ellka.model.project;

import lombok.*;
import ua.ellka.model.task.Task;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
public class Project {
    private Long id;
    private String name;
    private String description;
    private int priority;
    private ProjectStatus status;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
    private LocalDate endDate;
    private LocalDate deadline;
    private Manager manager;

    @Builder.Default
    private Set<Task> tasks = new HashSet<>();

    @Builder.Default
    private Set<Employee> employees = new HashSet<>();
}
