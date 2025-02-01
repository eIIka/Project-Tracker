package ua.ellka.service;

import ua.ellka.dto.ProjectDTO;

import java.util.List;

public interface ProjectService {

    /**
     * Creates a new project.
     *
     * @param projectDTO the project data transfer object.
     * @return the created project.
     */
    ProjectDTO createProject(ProjectDTO projectDTO);

    /**
     * Updates an existing project.
     *
     * @param projectDTO the project data transfer object with updated information.
     * @return the updated project.
     */
    ProjectDTO updateProject(ProjectDTO projectDTO);

    /**
     * Deletes a project.
     *
     * @param projectDTO the project to delete.
     * @return the deleted project.
     */
    ProjectDTO deleteProject(ProjectDTO projectDTO);

    /**
     * Retrieves all projects.
     *
     * @return a list of all projects.
     */
    //List<ProjectDTO> getAllProjects();

    /**
     * Retrieves a project by ID.
     *
     * @param id the project ID.
     * @return the found project.
     */
    ProjectDTO getProject(Long id);

    /**
     * Assigns a user to a project.
     *
     * @param projectId the project ID.
     * @param userId the user ID.
     * @return true if the user was successfully assigned, false otherwise.
     */
    boolean assignUserToProject(Long projectId, Long userId);

    /**
     * Removes a user from a project.
     *
     * @param projectId the project ID.
     * @param userId the user ID.
     * @return true if the user was successfully removed, false otherwise.
     */
    boolean removeUserFromProject(Long projectId, Long userId);
}
