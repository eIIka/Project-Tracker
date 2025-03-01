package ua.ellka.model.user;

import jakarta.persistence.*;
import lombok.*;
import ua.ellka.model.project.Project;
import ua.ellka.model.task.Task;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@DiscriminatorValue("Employee")
public class Employee extends User {
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "project_employees",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects = new HashSet<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Task> tasks = new HashSet<>();

    @Override
    public UserRole getRole() {
        return UserRole.EMPLOYEE;
    }
}
