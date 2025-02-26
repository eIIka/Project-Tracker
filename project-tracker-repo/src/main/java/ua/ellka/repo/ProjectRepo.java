package ua.ellka.repo;

import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.model.project.Project;
import ua.ellka.model.user.User;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for handling Project persistence operations.
 */
public interface ProjectRepo {

    /**
     * Finds a project by ID.
     *
     * @param id the ID of the project.
     * @return an Optional containing the found project or empty if not found.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    Optional<Project> find(Long id) throws ProjectTrackerPersistingException;

    /**
     * Finds a project by name.
     *
     * @param name the name of the project.
     * @return an Optional containing the found project or empty if not found.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    Optional<Project> findByName(String name) throws ProjectTrackerPersistingException;

    List<Project> findByUser(User user) throws ProjectTrackerPersistingException;

    /**
     * Saves a project to the database.
     *
     * @param project the project to save.
     * @return an Optional containing the saved project.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    Optional<Project> save(Project project) throws ProjectTrackerPersistingException;

    /**
     * Deletes a project by project.
     *
     * @param project the project to delete.
     * @return an Optional containing the deleted project or empty if not found.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    Optional<Project> delete(Project project) throws ProjectTrackerPersistingException;

    /**
     * Updates an existing project in the database.
     *
     * @param project the project object that needs to be updated
     * @return Optional containing the updated project, if successful
     * @throws ProjectTrackerPersistingException if the project is not found or any other error occurs during the update
     */
    Optional<Project> update(Project project) throws ProjectTrackerPersistingException;
}
