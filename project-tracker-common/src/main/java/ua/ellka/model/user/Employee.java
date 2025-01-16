package ua.ellka.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ellka.model.project.Project;
import ua.ellka.model.task.Task;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends User {
    Map<Project, Set<Task>> tasks = new HashMap<>();

    @Override
    public UserRole getRole() {
        return UserRole.EMPLOYEE;
    }
}
