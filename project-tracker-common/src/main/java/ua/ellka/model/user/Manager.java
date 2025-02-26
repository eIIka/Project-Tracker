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
@Entity
@DiscriminatorValue("Manager")
public class Manager extends User {
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Project> projects = new HashSet<>();

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks = new HashSet<>();

    @Override
    public UserRole getRole() {
        return UserRole.MANAGER;
    }
}
