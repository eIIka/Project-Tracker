package ua.ellka.model.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ellka.model.project.Project;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;
import ua.ellka.model.user.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private Long id;
    private String name;
    private String description;
    private TaskStatus pendingStatus;
    private String executionDetails;
    private TaskStatus status;
    private String type;
    private int priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate endDate;
    private LocalDate deadline;
    private List<String> history = new ArrayList<>();
    private Set<String> comments = new HashSet<>();
    private Manager manager;
    private Employee employee;
    private Project project;

    public void requestStatusChange(TaskStatus newStatus, String details) {
        if (this.pendingStatus != null) {
            throw new IllegalStateException("There is already a pending status change request");
        }
        this.pendingStatus = newStatus;
        this.executionDetails = details;
        this.history.add(LocalDateTime.now() + ": Requested status change to " + newStatus);
    }

    public void approveStatusChange() {
        manager.validateRole(UserRole.MANAGER);
        if (this.pendingStatus == null) {
            throw new IllegalStateException("No pending status change request");
        }
        this.history.add(LocalDateTime.now() + ": Status changed from " + this.status + " to " + this.pendingStatus);
        this.status = this.pendingStatus;
        this.pendingStatus = null;
    }

    public void rejectStatusChange() {
        manager.validateRole(UserRole.MANAGER);
        if (this.pendingStatus == null) {
            throw new IllegalStateException("No pending status change request");
        }
        this.history.add(LocalDateTime.now() + ": Status change request rejected");
        this.pendingStatus = null;
    }
}
