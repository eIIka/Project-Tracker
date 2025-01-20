package ua.ellka.model.task;

import jakarta.persistence.*;
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
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Convert(converter = TaskStatusConvertor.class)
    @Column(name = "pending_status")
    private TaskStatus pendingStatus;

    @Column(name = "execution_details")
    private String executionDetails;

    @Convert(converter = TaskStatusConvertor.class)
    private TaskStatus status;

    private String type;
    private int priority;

    @Column (name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column (name = "update_at")
    private LocalDateTime updatedAt;

    @Column (name = "end_date")
    private LocalDate endDate;

    private LocalDate deadline;

    @ElementCollection
    @CollectionTable(name = "task_history", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "history_entry")
    private List<String> history = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "task_comment", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "comment")
    private Set<String> comments = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = true)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = true)
    private Manager manager;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = true)
    private Project project;
}
