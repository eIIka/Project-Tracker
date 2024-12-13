package ua.ellka.model.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ellka.model.task.Task;
import ua.ellka.model.user.User;
import ua.ellka.model.user.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    private Long id;
    private String name;
    private String description;
    private int priority;
    private ProjectStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate endDate;
    private LocalDate deadline;
    private User manager;
    private Set<Task> tasks = new HashSet<>();
    private Set<User> users = new HashSet<>();
}
