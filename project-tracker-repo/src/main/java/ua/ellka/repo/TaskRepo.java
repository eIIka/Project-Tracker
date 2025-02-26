package ua.ellka.repo;

import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.model.project.Project;
import ua.ellka.model.task.Task;
import ua.ellka.model.user.User;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for handling Task persistence operations.
 */
public interface TaskRepo {

    /**
     * Finds a task by ID.
     *
     * @param id the ID of the task.
     * @return an Optional containing the found task or empty if not found.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    Optional<Task> find(Long id) throws ProjectTrackerPersistingException;

    /**
     * Finds a task by name.
     *
     * @param name the name of the task.
     * @return an Optional containing the found task or empty if not found.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    Optional<Task> findByName(String name) throws ProjectTrackerPersistingException;

    List<Task> findByUser(User user) throws ProjectTrackerPersistingException;

    List<Task> findByProject(Project project) throws ProjectTrackerPersistingException;

    List<Task> findByProjectAndUser(Project project, User user) throws ProjectTrackerPersistingException;

    /**
     * Saves a task to the database.
     *
     * @param task the task to save.
     * @return an Optional containing the saved task.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    Optional<Task> save(Task task) throws ProjectTrackerPersistingException;

    /**
     * Deletes a task from the database.
     *
     * @param task the task to delete.
     * @return an Optional containing the deleted task or empty if the task was not found.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    Optional<Task> delete(Task task) throws ProjectTrackerPersistingException;

    /**
     * Updates an existing task in the database.
     *
     * @param task The Task object containing updated data.
     * @return An Optional containing the updated task if it exists in the database
     * @throws ProjectTrackerPersistingException If an error occurs during the update process.
     */
    Optional<Task> update(Task task) throws ProjectTrackerPersistingException;
}
