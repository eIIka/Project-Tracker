package ua.ellka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.ellka.dto.TaskDTO;
import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.mapper.TaskMapper;
import ua.ellka.model.task.Task;
import ua.ellka.model.user.Employee;
import ua.ellka.repo.TaskRepo;
import ua.ellka.repo.UserRepo;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    
    private final TaskRepo taskRepo;
    private final TaskMapper taskMapper;
    private final UserRepo userRepo;
    
    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        try {
            Optional<Task> taskOptional = taskRepo.findByName(taskDTO.getName());
            if (taskOptional.isPresent() && taskDTO.getProjectName().equals(taskOptional.get().getProject().getName())) {
                throw new ProjectTrackerPersistingException(taskDTO, "Task with this name already exists in project", null);
            }

            Task task = taskMapper.taskDTOToTask(taskDTO);

            Optional<Task> saved = taskRepo.save(task);

            return taskMapper.taskToTaskDTO(saved.get());

        } catch (ProjectTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TaskDTO updateTask(TaskDTO taskDTO) {
        try {
            if (taskRepo.find(taskDTO.getId()).isEmpty()) {
                throw new ProjectTrackerPersistingException(taskDTO, "Task not found", null);
            }

            Task task = taskMapper.taskDTOToTask(taskDTO);

            Optional<Task> updated = taskRepo.update(task);

            return taskMapper.taskToTaskDTO(updated.get());

        } catch (ProjectTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TaskDTO getTask(Long id) {
        try {
            Optional<Task> task = taskRepo.find(id);

            return taskMapper.taskToTaskDTO(task.get());

        } catch (ProjectTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public List<TaskDTO> getAllTasksByProjectId(Long projectId) {
//        return List.of();
//    }

    @Override
    public TaskDTO deleteTask(Long id) {
        try {
            Task task = taskRepo.find(id).orElse(null);
            if (task == null) {
                throw new ProjectTrackerPersistingException(task, "Project not found", null);
            }

            Optional<Task> deleted = taskRepo.delete(task);

            return taskMapper.taskToTaskDTO(deleted.get());

        } catch (ProjectTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean assignUserToTask(Long taskId, Long userId) {
        try {
            Optional<Task> taskOptional = taskRepo.find(taskId);
            Optional<Employee> employeeOptional = userRepo.find(userId)
                    .filter(user -> user instanceof Employee)
                    .map(e -> (Employee) e );

            if (taskOptional.isEmpty() || employeeOptional.isEmpty()) {
                return false;
            }

            Task task = taskOptional.get();
            Employee employee = employeeOptional.get();

            if (task.getEmployee().equals(employee)) {
                return false;
            }

            task.setEmployee(employee);
            taskRepo.update(task);
            return true;

        } catch (ProjectTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean removeUserFromTask(Long taskId, Long userId) {
        try {
            Optional<Task> taskOptional = taskRepo.find(taskId);
            Optional<Employee> employeeOptional = userRepo.find(userId)
                    .filter(user -> user instanceof Employee)
                    .map(e -> (Employee) e );

            if (taskOptional.isEmpty() || employeeOptional.isEmpty()) {
                return false;
            }

            Task task = taskOptional.get();
            Employee employee = employeeOptional.get();

            if (!task.getEmployee().equals(employee)) {
                return false;
            }

            task.setEmployee(null);
            taskRepo.update(task);
            return true;

        } catch (ProjectTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }
}
