package ua.ellka.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.ellka.model.project.Project;
import ua.ellka.model.user.User;

import java.util.List;
import java.util.Optional;

public interface ProjectRepo extends JpaRepository<Project, Long> {

    Optional<Project> findByName(String name);

    List<Project> findByUser(User user);

}
