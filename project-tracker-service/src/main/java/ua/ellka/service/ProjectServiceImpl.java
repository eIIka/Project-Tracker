package ua.ellka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ellka.dto.ProjectDTO;
import ua.ellka.exception.NotFoundServiceException;
import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.exception.ServiceException;
import ua.ellka.mapper.ProjectMapper;
import ua.ellka.model.project.Project;
import ua.ellka.model.project.ProjectStatus;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;
import ua.ellka.model.user.User;
import ua.ellka.repo.ProjectRepo;
import ua.ellka.repo.UserRepo;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepo projectRepo;
    private final ProjectMapper projectMapper;
    private final UserRepo userRepo;

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        try {
            Manager manager = (Manager) userRepo.findByNickname(projectDTO.getManagerName())
                    .orElseThrow(() -> new NotFoundServiceException("Manager not found"));

            Project project = projectMapper.projectDTOToProject(projectDTO);

            if (manager.getProjects().contains(project)) {
                throw new ServiceException("Project already exists");
            }

            project.setManager(manager);

            Optional<Project> savedProject = projectRepo.save(project);
            Project saved = savedProject.orElseThrow(() -> new ServiceException("Project could not be created"));

            return projectMapper.projectToProjectDTO(saved);
        } catch (ProjectTrackerPersistingException e) {
            throw new NotFoundServiceException(e.getMessage());
        }
    }

    @Override
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        try {
            Project project = projectRepo.find(id)
                    .orElseThrow(() -> new NotFoundServiceException("Project not found"));

            project.setName(projectDTO.getName());
            project.setDescription(projectDTO.getDescription());
            project.setPriority(projectDTO.getPriority());
            project.setStatus(ProjectStatus.fromString(projectDTO.getStatus()));

            Project updated = projectRepo.update(project)
                    .orElseThrow(() -> new ServiceException("Project could not be updated"));

            return projectMapper.projectToProjectDTO(updated);
        } catch (ProjectTrackerPersistingException e) {
            throw new NotFoundServiceException(e.getMessage());
        }
    }

    @Override
    public ProjectDTO deleteProject(Long id) {
        try {
            Project project = projectRepo.find(id)
                    .orElseThrow(() -> new NotFoundServiceException("Project not found"));

            Project deleted = projectRepo.delete(project)
                    .orElseThrow(() -> new ServiceException("Project could not be deleted"));

            return projectMapper.projectToProjectDTO(deleted);
        } catch (ProjectTrackerPersistingException e) {
            throw new NotFoundServiceException(e.getMessage());
        }
    }

    @Override
    public List<ProjectDTO> getAllProjectsByUserId(Long userId) {
        try {
            User existingUser = userRepo.find(userId)
                    .orElseThrow(() -> new NotFoundServiceException("User not found"));

            return projectRepo.findByUser(existingUser)
                    .stream()
                    .map(projectMapper::projectToProjectDTO)
                    .toList();
        } catch (ProjectTrackerPersistingException e) {
            throw new NotFoundServiceException(e.getMessage());
        }
    }

    @Override
    public ProjectDTO getProject(Long id) {
        try {
            Project project = projectRepo.find(id)
                    .orElseThrow(() -> new NotFoundServiceException("Project not found"));

            return projectMapper.projectToProjectDTO(project);
        } catch (ProjectTrackerPersistingException e) {
            throw new NotFoundServiceException(e.getMessage());
        }
    }

    @Override
    public boolean assignUserToProject(Long projectId, Long userId) {
        try {
            Project project = projectRepo.find(projectId)
                    .orElseThrow(() -> new NotFoundServiceException("Project not found"));

            Employee employee = (Employee) userRepo.find(userId)
                    .orElseThrow(() -> new NotFoundServiceException("Employee not found"));

            if (project.getEmployees().contains(employee) || employee.getProjects().contains(project)) {
                throw new ServiceException("User is already assigned to this project");
            }

            project.getEmployees().add(employee);
            employee.getProjects().add(project);

            if (project.getEmployees().contains(employee) && employee.getProjects().contains(project)) {
                userRepo.update(employee);
                return true;
            }

            return false;
        } catch (ProjectTrackerPersistingException e) {
            throw new NotFoundServiceException(e.getMessage());
        }
    }

    @Override
    public boolean removeUserFromProject(Long projectId, Long userId) {
        try {
            Project project = projectRepo.find(projectId)
                    .orElseThrow(() -> new NotFoundServiceException("Project not found"));

            Employee employee = (Employee) userRepo.find(userId)
                    .orElseThrow(() -> new NotFoundServiceException("Employee not found"));

            if (!project.getEmployees().contains(employee) || !employee.getProjects().contains(project)) {
                throw new ServiceException("User is not assigned to this project");
            }

            project.getEmployees().remove(employee);
            employee.getProjects().remove(project);

            if (!project.getEmployees().contains(employee) && !employee.getProjects().contains(project)) {
                userRepo.update(employee);
                return true;
            }

            return false;
        } catch (ProjectTrackerPersistingException e) {
            throw new NotFoundServiceException(e.getMessage());
        }
    }
}
