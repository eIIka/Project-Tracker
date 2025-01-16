package ua.ellka.model.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ellka.model.project.Project;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private Long id;
    private String name;
    private String description;
    private TaskStatus pendingStatus;
    private String executionDetails;
    private TaskStatus status;
    private String type;
    private int priority;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    private LocalDate endDate;
    private LocalDate deadline;

    private List<String> history = new ArrayList<>();
    private Set<String> comments = new HashSet<>();

    private Manager manager;
    private Employee employee;
    private Project project;
}
