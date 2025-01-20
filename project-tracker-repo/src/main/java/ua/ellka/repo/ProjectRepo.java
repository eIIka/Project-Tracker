package ua.ellka.repo;

import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.model.project.Project;

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

    /**
     * Saves a project to the database.
     *
     * @param project the project to save.
     * @return an Optional containing the saved project.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    Optional<Project> save(Project project) throws ProjectTrackerPersistingException;

    /**
     * Updates a project in the database.
     *
     * @param project the project to update.
     * @return an Optional containing the updated project or empty if the project was not found.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    Optional<Project> update(Project project) throws ProjectTrackerPersistingException;

    /**
     * Deletes a project by ID.
     *
     * @param id the ID of the project to delete.
     * @return an Optional containing the deleted project or empty if not found.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    Optional<Project> delete(Long id) throws ProjectTrackerPersistingException;

}
