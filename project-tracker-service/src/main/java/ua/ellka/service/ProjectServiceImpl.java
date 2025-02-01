package ua.ellka.service;

import lombok.RequiredArgsConstructor;
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
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepo projectRepo;
    private final ProjectMapper projectMapper;
    private final UserRepo userRepo;

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        try {
            if (projectRepo.findByName(projectDTO.getName()).isPresent()) {
                throw new ProjectTrackerPersistingException(projectDTO, "Project with this name already exists", null);
            }

            Project project = projectMapper.projectDTOToProject(projectDTO);

            Optional<Project> saved = projectRepo.save(project);

            return projectMapper.projectToProjectDTO(saved.get());

        } catch (ProjectTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProjectDTO updateProject(ProjectDTO projectDTO) {
        try {
            if (projectRepo.find(projectDTO.getId()).isEmpty()) {
                throw new ProjectTrackerPersistingException(projectDTO, "Project not found", null);
            }

            Project project = projectMapper.projectDTOToProject(projectDTO);

            Optional<Project> updated = projectRepo.update(project);

            return projectMapper.projectToProjectDTO(updated.get());

        } catch (ProjectTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProjectDTO deleteProject(ProjectDTO projectDTO) {
        try {
            if (projectRepo.findByName(projectDTO.getName()).isEmpty()) {
                throw new ProjectTrackerPersistingException(projectDTO, "Project not found", null);
            }

            Project project = projectMapper.projectDTOToProject(projectDTO);

            Optional<Project> deleted = projectRepo.delete(project.getId());

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
        try {
            Optional<Project> project = projectRepo.find(id);

            return projectMapper.projectToProjectDTO(project.get());

        } catch (ProjectTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean assignUserToProject(Long projectId, Long userId) {
        try {
            Optional<Project> projectOptional = projectRepo.find(projectId);
            Optional<Employee> employeeOptional = userRepo.find(userId)
                    .filter(user -> user instanceof Employee)
                    .map(e -> (Employee) e);

            if (projectOptional.isEmpty() || employeeOptional.isEmpty()) {
                return false;
            }

            Project project = projectOptional.get();
            Employee employee = employeeOptional.get();

            if (project.getEmployees().contains(employee)) {
                return false;
            }

            project.getEmployees().add(employee);
            projectRepo.update(project);
            return true;

        } catch (ProjectTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean removeUserFromProject(Long projectId, Long userId) {
        try {
            Optional<Project> projectOptional = projectRepo.find(projectId);
            Optional<Employee> employeeOptional = userRepo.find(userId)
                    .filter(user -> user instanceof Employee)
                    .map(e -> (Employee) e);

            if (projectOptional.isEmpty() || employeeOptional.isEmpty()) {
                return false;
            }

            Project project = projectOptional.get();
            Employee employee = employeeOptional.get();

            if (project.getEmployees().remove(employee)) {
                projectRepo.update(project);
                return true;
            }

            return false;

        } catch (ProjectTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }
}
