package ua.ellka.model.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ua.ellka.model.project.Project;
import ua.ellka.model.task.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@SuperBuilder

public class Employee extends User {
    @Builder.Default
    Map<Project, Set<Task>> tasks = new HashMap<>();

}
