package ua.ellka.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.ellka.model.project.Project;
import ua.ellka.model.task.Task;
import ua.ellka.model.user.User;

import java.util.List;
import java.util.Optional;

public interface TaskRepo extends JpaRepository<Task, Long> {

    Optional<Task> findByName(String name);

    List<Task> findByUser(User user);

    List<Task> findByProject(Project project);

    List<Task> findByProjectAndUser(Project project, User user);

}
