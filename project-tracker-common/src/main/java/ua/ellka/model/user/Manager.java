package ua.ellka.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ellka.model.project.Project;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("Manager")
public class Manager extends User {
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    Set<Project> projects = new HashSet<>();

    @Override
    public UserRole getRole() {
        return UserRole.MANAGER;
    }
}
