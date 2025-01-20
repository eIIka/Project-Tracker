package ua.ellka.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ellka.model.project.Project;
import ua.ellka.model.task.Task;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("Employee")
public class Employee extends User {
    @ManyToMany
    @JoinTable(
            name = "project_employees",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")

    )
    private Set<Project> projects = new HashSet<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Task> tasks = new HashSet<>();

    @Override
    public UserRole getRole() {
        return UserRole.EMPLOYEE;
    }
}
