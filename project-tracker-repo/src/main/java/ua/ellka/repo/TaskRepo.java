package ua.ellka.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.ellka.model.project.Project;
import ua.ellka.model.task.Task;
import ua.ellka.model.user.User;

import java.util.List;
import java.util.Optional;

public interface TaskRepo extends JpaRepository<Task, Long> {

    Optional<Task> findByName(String name);


    @Query("""
                SELECT t FROM Task t
                WHERE t.employee = :user OR t.manager = :user
            """)
    List<Task> findByUser(@Param("user") User user);

    List<Task> findByProject(Project project);

    @Query("""
                SELECT t FROM Task t
                WHERE t.project = :project AND (t.employee = :user OR t.manager = :user)
            """)
    List<Task> findByProjectAndUser(@Param("project") Project project, @Param("user") User user);

}
