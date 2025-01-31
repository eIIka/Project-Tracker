package ua.ellka.service;

import ua.ellka.dto.TaskDTO;

import java.util.List;

public interface TaskService {

    /**
     * Creates a new task.
     *
     * @param taskDTO the task data transfer object.
     * @return the created task.
     */
    TaskDTO createTask(TaskDTO taskDTO);

    /**
     * Updates an existing task.
     *
     * @param taskDTO the task data transfer object with updated information.
     * @return the updated task.
     */
    TaskDTO updateTask(TaskDTO taskDTO);

    /**
     * Retrieves a task by ID.
     *
     * @param id the task ID.
     * @return the found task.
     */
    TaskDTO getTask(Long id);

    /**
     * Retrieves all tasks associated with a project.
     *
     * @param projectId the project ID.
     * @return a list of tasks related to the project.
     */
    List<TaskDTO> getAllTasksByProjectId(Long projectId);

    /**
     * Deletes a task by ID.
     *
     * @param id the ID of the task to delete.
     * @return the deleted task.
     */
    TaskDTO deleteTask(Long id);

    /**
     * Assigns a user to a task.
     *
     * @param taskId the task ID.
     * @param userId the user ID.
     * @return true if the user was successfully assigned, false otherwise.
     */
    boolean assignUserToTask(Long taskId, Long userId);

    /**
     * Removes a user from a task.
     *
     * @param taskId the task ID.
     * @param userId the user ID.
     * @return true if the user was successfully removed, false otherwise.
     */
    boolean removeUserFromTask(Long taskId, Long userId);
}
