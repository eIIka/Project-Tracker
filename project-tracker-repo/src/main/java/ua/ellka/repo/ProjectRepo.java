package ua.ellka.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.ellka.model.project.Project;
import ua.ellka.model.user.User;

import java.util.List;
import java.util.Optional;

public interface ProjectRepo extends JpaRepository<Project, Long> {

    Optional<Project> findByName(String name);

    @Query("""
                SELECT p FROM Project p
                LEFT JOIN p.employees e
                WHERE e = :user OR p.manager = :user
            """)
    List<Project> findByUser(@Param("user") User user);

}
