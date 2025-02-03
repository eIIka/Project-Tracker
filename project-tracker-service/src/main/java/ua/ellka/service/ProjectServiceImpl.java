package ua.ellka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.ellka.dto.ProjectDTO;
import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.mapper.ProjectMapper;
import ua.ellka.model.project.Project;
import ua.ellka.model.user.Employee;
import ua.ellka.repo.ProjectRepo;
import ua.ellka.repo.UserRepo;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepo projectRepo;
    private final ProjectMapper projectMapper;
    private final UserRepo userRepo;

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        log.info("Create a project");
        log.debug("projectDTO: {}", projectDTO);

        try {
            if (projectRepo.findByName(projectDTO.getName()).isPresent()) {
                log.warn("Project with name {} already exists", projectDTO.getName());
                throw new ProjectTrackerPersistingException(projectDTO, "Project with this name already exists", null);
            }

            Project project = projectMapper.projectDTOToProject(projectDTO);
            log.info("Mapping projectDTO to project");
            log.debug("project: {}", project);

            Optional<Project> saved = projectRepo.save(project);
            log.info("Saved project");
            log.debug("saved: {}", saved);

            return projectMapper.projectToProjectDTO(saved.get());

        } catch (ProjectTrackerPersistingException e) {
            log.error("Failed to create project: {}", projectDTO.getName(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProjectDTO updateProject(ProjectDTO projectDTO) {
        log.info("Update a project");
        log.debug("projectDTO: {}", projectDTO);

        try {
            if (projectRepo.find(projectDTO.getId()).isEmpty()) {
                log.warn("Project with id {} does not exist", projectDTO.getId());
                throw new ProjectTrackerPersistingException(projectDTO, "Project not found", null);
            }

            Project project = projectMapper.projectDTOToProject(projectDTO);
            log.info("Mapping projectDTO to project");
            log.debug("project: {}", project);

            Optional<Project> updated = projectRepo.update(project);
            log.info("Updated project");
            log.debug("updated: {}", updated);

            return projectMapper.projectToProjectDTO(updated.get());

        } catch (ProjectTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProjectDTO deleteProject(ProjectDTO projectDTO) {
        log.info("Delete a project");
        log.debug("projectDTO: {}", projectDTO);

        try {
            if (projectRepo.findByName(projectDTO.getName()).isEmpty()) {
                log.warn("Project with name {} does not exist", projectDTO.getName());
                throw new ProjectTrackerPersistingException(projectDTO, "Project not found", null);
            }

            Project project = projectMapper.projectDTOToProject(projectDTO);
            log.info("Mapping projectDTO to project");
            log.debug("project: {}", project);

            Optional<Project> deleted = projectRepo.delete(project.getId());
            log.info("Deleted project");
            log.debug("deleted: {}", deleted);

            return projectMapper.projectToProjectDTO(deleted.get());

        } catch (ProjectTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public List<ProjectDTO> getAllProjects() {
//
//        return List.of();
//    }

    @Override
    public ProjectDTO getProject(Long id) {
        log.info("Get a project");
        log.debug("id: {}", id);

        try {
            Optional<Project> project = projectRepo.find(id);
            log.info("Found project");
            log.debug("project: {}", project);

            return projectMapper.projectToProjectDTO(project.get());

        } catch (ProjectTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean assignUserToProject(Long projectId, Long userId) {
        log.info("Assign a user to a project");
        log.debug("projectId: {}\nuserId: {}", projectId, userId);

        try {
            Optional<Project> projectOptional = projectRepo.find(projectId);

            Optional<Employee> employeeOptional = userRepo.find(userId)
                    .filter(user -> user instanceof Employee)
                    .map(e -> (Employee) e);
            log.info("Found project & employee");
            log.debug("project: {}\nemployee: {}", projectOptional, employeeOptional);

            if (projectOptional.isEmpty() || employeeOptional.isEmpty()) {
                log.warn("Project with id {} does not exist", projectId);
                return false;
            }

            Project project = projectOptional.get();
            Employee employee = employeeOptional.get();

            if (project.getEmployees().contains(employee)) {
                log.warn("Employee with id {} already exists", projectId);
                return false;
            }

            project.getEmployees().add(employee);
            log.info("Added employee to project");

            projectRepo.update(project);
            log.info("Updated project");

            return true;

        } catch (ProjectTrackerPersistingException e) {
            log.error("Failed to assign a user to a project: {}", projectId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean removeUserFromProject(Long projectId, Long userId) {
        log.info("Remove a user from a project");
        log.debug("projectId: {}\nuserId: {}", projectId, userId);

        try {
            Optional<Project> projectOptional = projectRepo.find(projectId);
            Optional<Employee> employeeOptional = userRepo.find(userId)
                    .filter(user -> user instanceof Employee)
                    .map(e -> (Employee) e);
            log.info("Found project & employee");
            log.debug("project: {}\nemployee: {}", projectOptional, employeeOptional);

            if (projectOptional.isEmpty() || employeeOptional.isEmpty()) {
                log.warn("Project with id {} does not exist", projectId);
                return false;
            }

            Project project = projectOptional.get();
            Employee employee = employeeOptional.get();

            if (project.getEmployees().remove(employee)) {
                log.info("Removed employee from project");

                projectRepo.update(project);
                log.info("Updated project");

                return true;
            }

            return false;

        } catch (ProjectTrackerPersistingException e) {
            log.error("Failed to remove a user from a project: {}", projectId, e);
            throw new RuntimeException(e);
        }
    }
}
