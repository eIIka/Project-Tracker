package ua.ellka.model.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ua.ellka.model.project.Project;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@SuperBuilder

public class Manager extends User {
    @Builder.Default
    Set<Project> projects = new HashSet<>();

}
