package ua.ellka.model.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ellka.model.project.Project;
import ua.ellka.model.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private Long id;
    private String name;
    private String description;
    private TaskStatus status;
    private String type;
    private int priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate endDate;
    private LocalDate deadline;
    private Set<String> comments = new HashSet<>();
    private User user;
    private Project project;
}
