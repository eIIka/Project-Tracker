package ua.ellka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.ellka.dto.ProjectDTO;
import ua.ellka.exception.NotFoundServiceException;
import ua.ellka.exception.ServiceException;
import ua.ellka.mapper.ProjectMapper;
import ua.ellka.model.project.Project;
import ua.ellka.model.project.ProjectStatus;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;
import ua.ellka.model.user.User;
import ua.ellka.model.user.UserRole;
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
    @PreAuthorize("#projectDTO.managerName == authentication.principal.nickname")
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        try {
            Manager manager = (Manager) userRepo.findByNickname(projectDTO.getManagerName())
                    .orElseThrow(() -> new NotFoundServiceException("Manager not found"));

            Project project = projectMapper.projectDTOToProject(projectDTO);

            if (manager.getProjects().contains(project)) {
                throw new ServiceException("Project already exists");
            }

            project.setManager(manager);

            Project saved = projectRepo.save(project);

            return projectMapper.projectToProjectDTO(saved);
        } catch (ServiceException e){
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error while creating project: " + e.getMessage());
        }
    }

    @Override
    @PreAuthorize("#projectDTO.managerName == authentication.principal.nickname")
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        try {
            Project project = projectRepo.findById(id)
                    .orElseThrow(() -> new NotFoundServiceException("Project not found"));

            project.setName(projectDTO.getName());
            project.setDescription(projectDTO.getDescription());
            project.setPriority(projectDTO.getPriority());
            project.setStatus(ProjectStatus.fromString(projectDTO.getStatus()));

            Project updated = projectRepo.save(project);

            return projectMapper.projectToProjectDTO(updated);
        } catch (ServiceException e){
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error while updating project by id: " + e.getMessage());
        }
    }

    @Override
    public ProjectDTO deleteProject(Long id) {
        try {
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

            User existingUser = userRepo.findByEmail(currentUser)
                    .orElseThrow(() -> new NotFoundServiceException("User not found"));
            if (existingUser.getRole() == UserRole.EMPLOYEE){
                throw new ServiceException("Employee cannot be delete project");
            }

            Project project = projectRepo.findById(id)
                    .orElseThrow(() -> new NotFoundServiceException("Project not found"));

            Manager manager = (Manager) existingUser;
            if (manager.getProjects().contains(project) || !project.getManager().equals(manager)){
                throw new ServiceException("The user cannot delete the project because they are not the project manager");
            }

            projectRepo.delete(project);

            return projectMapper.projectToProjectDTO(project);
        } catch (ServiceException e){
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error while deleting project by id: " + e.getMessage());
        }
    }

    @Override
    @PreAuthorize("#userId == authentication.principal.id")
    public List<ProjectDTO> getAllProjectsByUserId(Long userId) {
        try {
            User existingUser = userRepo.findById(userId)
                    .orElseThrow(() -> new NotFoundServiceException("User not found"));

            return projectRepo.findByUser(existingUser)
                    .stream()
                    .map(projectMapper::projectToProjectDTO)
                    .toList();
        } catch (Exception e) {
            throw new NotFoundServiceException(e.getMessage());
        }
    }

    @Override
//    @PostAuthorize("returnObject.managerName == authentication.principal.nickname " +
//                   "|| returnObject.employees.contains(authentication.principal.nickname)")
    public ProjectDTO getProject(Long id) {
        try {
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

            Project project = projectRepo.findById(id)
                    .orElseThrow(() -> new NotFoundServiceException("Project not found"));

            boolean isManager = project.getManager().getNickname().equals(currentUser);
            boolean isEmployee = project.getEmployees().stream()
                    .anyMatch(employee -> employee.getNickname().equals(currentUser));

            if (!isManager && !isEmployee) {
                throw new NotFoundServiceException("Access denied: User is not part of the project");
            }

            return projectMapper.projectToProjectDTO(project);
        } catch (Exception e) {
            throw new NotFoundServiceException(e.getMessage());
        }
    }

    @Override
    public boolean assignUserToProject(Long projectId, Long userId) {
        try {
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

            User existingUser = userRepo.findByEmail(currentUser)
                    .orElseThrow(() -> new NotFoundServiceException("User not found"));
            if (existingUser.getRole() == UserRole.EMPLOYEE){
                throw new ServiceException("Employee cannot be assign user to project");
            }

            Project project = projectRepo.findById(projectId)
                    .orElseThrow(() -> new NotFoundServiceException("Project not found"));

            Employee employee = (Employee) userRepo.findById(userId)
                    .orElseThrow(() -> new NotFoundServiceException("Employee not found"));

            if (project.getEmployees().contains(employee) || employee.getProjects().contains(project)) {
                throw new ServiceException("User is already assigned to this project");
            }

            project.getEmployees().add(employee);
            employee.getProjects().add(project);

            if (project.getEmployees().contains(employee) && employee.getProjects().contains(project)) {
                userRepo.save(employee);
                return true;
            }

            return false;
        } catch (ServiceException e){
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error while assigning user to project: " + e.getMessage());
        }
    }

    @Override
    public boolean removeUserFromProject(Long projectId, Long userId) {
        try {
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

            User existingUser = userRepo.findByEmail(currentUser)
                    .orElseThrow(() -> new NotFoundServiceException("User not found"));
            if (existingUser.getRole() == UserRole.EMPLOYEE){
                throw new ServiceException("Employee cannot be remove user from project");
            }

            Project project = projectRepo.findById(projectId)
                    .orElseThrow(() -> new NotFoundServiceException("Project not found"));

            Employee employee = (Employee) userRepo.findById(userId)
                    .orElseThrow(() -> new NotFoundServiceException("Employee not found"));

            if (!project.getEmployees().contains(employee) || !employee.getProjects().contains(project)) {
                throw new ServiceException("User is not assigned to this project");
            }

            project.getEmployees().remove(employee);
            employee.getProjects().remove(project);

            if (!project.getEmployees().contains(employee) && !employee.getProjects().contains(project)) {
                projectRepo.save(project);
                userRepo.save(employee);
                return true;
            }

            return false;
        } catch (ServiceException e){
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error while removing user from project: " + e.getMessage());
        }
    }
}
