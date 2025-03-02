package ua.ellka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ellka.dto.TaskDTO;
import ua.ellka.exception.NotFoundServiceException;
import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.exception.ServiceException;
import ua.ellka.mapper.TaskMapper;
import ua.ellka.model.project.Project;
import ua.ellka.model.task.Task;
import ua.ellka.model.task.TaskStatus;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;
import ua.ellka.model.user.User;
import ua.ellka.repo.ProjectRepo;
import ua.ellka.repo.TaskRepo;
import ua.ellka.repo.UserRepo;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepo taskRepo;
    private final TaskMapper taskMapper;
    private final UserRepo userRepo;
    private final ProjectRepo projectRepo;

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        try {
            Project project = projectRepo.findByName(taskDTO.getProjectName())
                    .orElseThrow(() -> new NotFoundServiceException("Project not found"));

            List<Task> tasksInProject = taskRepo.findByProject(project);
            Task task = taskMapper.taskDTOToTask(taskDTO);

            if (tasksInProject.contains(task)) {
                    throw new ServiceException("Task already exists in this project");
            }

            Employee employee = (Employee) userRepo.findByNickname(taskDTO.getAssignedEmployee())
                    .orElseThrow(() -> new NotFoundServiceException("Employee not found"));

            Manager manager = (Manager) userRepo.findByNickname(taskDTO.getAssignedManager())
                    .orElseThrow(() -> new NotFoundServiceException("Manager not found"));

            task.setProject(project);
            task.setManager(manager);
            task.setEmployee(employee);

            Task saved = taskRepo.save(task)
                    .orElseThrow(() -> new ServiceException("Task could not be saved"));

            return taskMapper.taskToTaskDTO(saved);
        } catch (ProjectTrackerPersistingException e) {
            throw new NotFoundServiceException(e.getMessage());
        }
    }

    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        try {
            Task task = taskRepo.find(id)
                    .orElseThrow(() -> new NotFoundServiceException("Task not found"));

            task.setName(taskDTO.getName());
            task.setDescription(taskDTO.getDescription());
            task.setStatus(TaskStatus.fromString(taskDTO.getStatus()));
            task.setType(taskDTO.getType());
            task.setPriority(taskDTO.getPriority());
            task.setDeadline(taskDTO.getDeadline());
            if (taskDTO.getAssignedEmployee() != null) {
                User employee = userRepo.findByNickname(taskDTO.getAssignedEmployee())
                        .orElseThrow(() -> new NotFoundServiceException("Employee not found"));
                task.setEmployee((Employee) employee);
            }

            Task updated = taskRepo.update(task)
                    .orElseThrow(() -> new ServiceException("Task could not be updated"));

            return taskMapper.taskToTaskDTO(updated);
        } catch (ProjectTrackerPersistingException e) {
            throw new NotFoundServiceException(e.getMessage());
        }
    }

    @Override
    public TaskDTO getTask(Long id) {
        try {
            Task task = taskRepo.find(id)
                    .orElseThrow(() -> new NotFoundServiceException("Task not found"));

            return taskMapper.taskToTaskDTO(task);
        } catch (ProjectTrackerPersistingException e) {
            throw new NotFoundServiceException(e.getMessage());
        }
    }

    @Override
    public List<TaskDTO> getAllTasksByProjectId(Long projectId) {
        try {
            Project existingProject = projectRepo.find(projectId)
                    .orElseThrow(() -> new NotFoundServiceException("Project not found"));

            return taskRepo.findByProject(existingProject)
                    .stream()
                    .map(taskMapper::taskToTaskDTO)
                    .toList();
        } catch (ProjectTrackerPersistingException e) {
            throw new NotFoundServiceException(e.getMessage());
        }
    }

    @Override
    public List<TaskDTO> getAllTasksByUserId(Long userId) {
        try {
            User existingUser = userRepo.find(userId)
                    .orElseThrow(() -> new NotFoundServiceException("User not found"));

            return taskRepo.findByUser(existingUser)
                    .stream()
                    .map(taskMapper::taskToTaskDTO)
                    .toList();
        } catch (ProjectTrackerPersistingException e) {
            throw new NotFoundServiceException(e.getMessage());
        }
    }

    @Override
    public List<TaskDTO> getAllTasksByProjectIdAndUserId(Long projectId, Long userId) {
        try {
            User existingUser = userRepo.find(userId)
                    .orElseThrow(() -> new NotFoundServiceException("User not found"));

            Project existingProject = projectRepo.find(projectId)
                    .orElseThrow(() -> new NotFoundServiceException("Project not found"));

            return taskRepo.findByProjectAndUser(existingProject, existingUser)
                    .stream()
                    .map(taskMapper::taskToTaskDTO)
                    .toList();
        } catch (ProjectTrackerPersistingException e) {
            throw new NotFoundServiceException(e.getMessage());
        }
    }

    @Override
    public TaskDTO deleteTask(Long id) {
        try {
            Task task = taskRepo.find(id)
                    .orElseThrow(() -> new NotFoundServiceException("Task not found"));

            Task deleted = taskRepo.delete(task)
                    .orElseThrow(() -> new ServiceException("Task could not be deleted"));

            return taskMapper.taskToTaskDTO(deleted);
        } catch (ProjectTrackerPersistingException e) {
            throw new NotFoundServiceException(e.getMessage());
        }
    }

    @Override
    public boolean assignUserToTask(Long taskId, Long userId) {
        try {
            Task task = taskRepo.find(taskId)
                    .orElseThrow(() -> new NotFoundServiceException("Task not found"));

            Employee employee = (Employee) userRepo.find(userId)
                    .orElseThrow(() -> new NotFoundServiceException("Employee not found"));

            if (employee.equals(task.getEmployee()) || employee.getTasks().contains(task)) {
                throw new ServiceException("Employee already assigned to task");
            }

            task.setEmployee(employee);
            employee.getTasks().add(task);
            if (employee.equals(task.getEmployee()) && employee.getTasks().contains(task)) {
                taskRepo.update(task);
                return true;
            }

            return false;
        } catch (ProjectTrackerPersistingException e) {
            throw new NotFoundServiceException(e.getMessage());
        }
    }

    @Override
    public boolean removeUserFromTask(Long taskId, Long userId) {
        try {
            Task task = taskRepo.find(taskId)
                    .orElseThrow(() -> new NotFoundServiceException("Task not found"));

            Employee employee = (Employee) userRepo.find(userId)
                    .orElseThrow(() -> new NotFoundServiceException("Employee not found"));

            if (task.getEmployee() == null || !task.getEmployee().equals(employee) || !employee.getTasks().contains(task)) {
                throw new ServiceException("Employee is not assigned to task");
            }

            task.setEmployee(null);
            employee.getTasks().remove(task);
            if (task.getEmployee() == null && !employee.getTasks().contains(task)) {
                taskRepo.update(task);
                return true;
            }

            return false;
        } catch (ProjectTrackerPersistingException e) {
            throw new NotFoundServiceException(e.getMessage());
        }
    }
}
