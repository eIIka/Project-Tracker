package ua.ellka.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ua.ellka.dto.TaskDTO;
import ua.ellka.exception.NotFoundServiceException;
import ua.ellka.exception.ServiceException;
import ua.ellka.mapper.TaskMapper;
import ua.ellka.model.project.Project;
import ua.ellka.model.task.Task;
import ua.ellka.model.task.TaskStatus;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;
import ua.ellka.model.user.UserRole;
import ua.ellka.repo.ProjectRepo;
import ua.ellka.repo.TaskRepo;
import ua.ellka.repo.UserRepo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {
    private TaskService taskService;
    private TaskRepo taskRepo;
    private ProjectRepo projectRepo;
    private UserRepo userRepo;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        taskRepo = mock(TaskRepo.class);
        userRepo = mock(UserRepo.class);
        projectRepo = mock(ProjectRepo.class);
        TaskMapper taskMapper = TaskMapper.INSTANCE;
        authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        taskService = new TaskServiceImpl(taskRepo, taskMapper, userRepo, projectRepo);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createTaskTest_success() throws Exception {
        Manager manager = new Manager();
        manager.setFirstName("Test");
        manager.setLastName("Test");
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Employee employee = new Employee();
        employee.setFirstName("Test");
        employee.setLastName("Test");
        employee.setNickname("Test Employee");
        employee.setEmail("test.employee@example.com");

        Project project = new Project();
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setManager(manager);

        TaskDTO testTaskDTO = new TaskDTO();
        testTaskDTO.setId(1L);
        testTaskDTO.setName("Test Task");
        testTaskDTO.setDescription("Test Description");
        testTaskDTO.setPriority(1);
        testTaskDTO.setStatus(TaskStatus.IN_PROGRESS.getStatus());
        testTaskDTO.setProjectName(project.getName());
        testTaskDTO.setAssignedManager(manager.getNickname());
        testTaskDTO.setAssignedEmployee(employee.getNickname());

        Task mockTask = new Task();
        mockTask.setId(1L);
        mockTask.setName("Test Task");
        mockTask.setDescription("Test Description");
        mockTask.setPriority(1);
        mockTask.setStatus(TaskStatus.IN_PROGRESS);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(userRepo.findByNickname("Test Employee")).thenReturn(Optional.of(employee));
        when(userRepo.findByNickname("Test Manager")).thenReturn(Optional.of(manager));
        when(taskRepo.findById(anyLong())).thenReturn(Optional.of(mockTask));
        when(projectRepo.findByName(anyString())).thenReturn(Optional.of(project));
        when(taskRepo.save(any())).thenReturn(mockTask);

        TaskDTO createdTaskDTO = taskService.createTask(testTaskDTO);

        assertNotNull(createdTaskDTO);
        assertEquals(testTaskDTO.getId(), createdTaskDTO.getId());
        assertEquals(testTaskDTO.getName(), createdTaskDTO.getName());
        assertEquals(testTaskDTO.getDescription(), createdTaskDTO.getDescription());
        assertEquals(testTaskDTO.getPriority(), createdTaskDTO.getPriority());
        assertEquals(testTaskDTO.getStatus(), createdTaskDTO.getStatus());
    }

    @Test
    void createTaskTest_existingTaskThrowsException() throws Exception {
        Manager manager = new Manager();
        manager.setFirstName("Test");
        manager.setLastName("Test");
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setManager(manager);

        TaskDTO testTaskDTO = new TaskDTO();
        testTaskDTO.setId(1L);
        testTaskDTO.setName("Test Task");
        testTaskDTO.setDescription("Test Description");
        testTaskDTO.setPriority(1);
        testTaskDTO.setStatus(TaskStatus.IN_PROGRESS.getStatus());
        testTaskDTO.setProjectName(project.getName());
        testTaskDTO.setAssignedManager(manager.getNickname());

        Task mockTask = new Task();
        mockTask.setId(1L);
        mockTask.setName("Test Task");
        mockTask.setDescription("Test Description");
        mockTask.setPriority(1);
        mockTask.setStatus(TaskStatus.IN_PROGRESS);
        mockTask.setProject(project);
        project.getTasks().add(mockTask);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(projectRepo.findByName(anyString())).thenReturn(Optional.of(project));
        when(taskRepo.findByProject(any())).thenReturn(List.of(mockTask));
        when(taskRepo.findById(anyLong())).thenReturn(Optional.of(mockTask));
        when(taskRepo.save(any())).thenReturn(mockTask);

        ServiceException exception = assertThrows(ServiceException.class,
                () -> taskService.createTask(testTaskDTO));
        assertEquals("Task already exists in this project", exception.getMessage());
    }

    @Test
    void updateTaskTest_success() throws Exception {
        Manager manager = new Manager();
        manager.setFirstName("Test");
        manager.setLastName("Test");
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Task mockTask = new Task();
        mockTask.setId(1L);
        mockTask.setName("Test Task");
        mockTask.setDescription("Test Description");
        mockTask.setPriority(1);
        mockTask.setStatus(TaskStatus.IN_PROGRESS);
        mockTask.setManager(manager);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(taskRepo.findById(anyLong())).thenReturn(Optional.of(mockTask));
        when(taskRepo.save(any())).thenReturn(mockTask);

        TaskDTO testTaskDTO = new TaskDTO();
        testTaskDTO.setName("Test New Task");
        testTaskDTO.setDescription("Test New Description");
        testTaskDTO.setPriority(2);
        testTaskDTO.setStatus(TaskStatus.NOT_STARTED.getStatus());
        testTaskDTO.setDeadline(LocalDate.now().plusDays(2));
        testTaskDTO.setAssignedManager(manager.getNickname());

        TaskDTO updated = taskService.updateTask(1L, testTaskDTO);

        assertNotNull(updated);
        assertNotNull(updated.getId());
        assertEquals(1L, updated.getId());
        assertEquals(testTaskDTO.getName(), updated.getName());
        assertEquals(testTaskDTO.getDescription(), updated.getDescription());
        assertEquals(testTaskDTO.getPriority(), updated.getPriority());
        assertEquals(testTaskDTO.getStatus(), updated.getStatus());
        assertEquals(testTaskDTO.getDeadline(), updated.getDeadline());
    }

    @Test
    void updateTaskTest_existingEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setId(2L);
        employee.setNickname("Test Employee");

        Task mockTask = new Task();
        mockTask.setId(1L);
        mockTask.setName("Test Task");
        mockTask.setDescription("Test Description");
        mockTask.setPriority(1);
        mockTask.setStatus(TaskStatus.IN_PROGRESS);

        when(userRepo.findByNickname(anyString())).thenReturn(Optional.of(employee));
        when(taskRepo.findById(anyLong())).thenReturn(Optional.of(mockTask));
        when(taskRepo.save(any())).thenReturn(mockTask);

        TaskDTO testTaskDTO = new TaskDTO();
        testTaskDTO.setName("Test New Task");
        testTaskDTO.setDescription("Test New Description");
        testTaskDTO.setPriority(2);
        testTaskDTO.setStatus(TaskStatus.NOT_STARTED.getStatus());
        testTaskDTO.setDeadline(LocalDate.now().plusDays(2));
        testTaskDTO.setAssignedEmployee(employee.getNickname());

        TaskDTO updated = taskService.updateTask(1L, testTaskDTO);

        assertNotNull(updated);
        assertNotNull(updated.getId());
        assertEquals(1L, updated.getId());
        assertEquals(testTaskDTO.getName(), updated.getName());
        assertEquals(testTaskDTO.getDescription(), updated.getDescription());
        assertEquals(testTaskDTO.getPriority(), updated.getPriority());
        assertEquals(testTaskDTO.getStatus(), updated.getStatus());
        assertEquals(testTaskDTO.getDeadline(), updated.getDeadline());
        assertEquals(testTaskDTO.getAssignedEmployee(), updated.getAssignedEmployee());
    }

    @Test
    void getTaskTest_success() throws Exception {
        Manager manager = new Manager();
        manager.setFirstName("Test");
        manager.setLastName("Test");
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Task task = new Task();
        task.setId(1L);
        task.setName("Test Task");
        task.setDescription("Test Description");
        task.setPriority(1);
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setManager(manager);

        when(taskRepo.findById(anyLong())).thenReturn(Optional.of(task));
        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));

        TaskDTO taskById = taskService.getTask(task.getId());

        assertNotNull(taskById);
        assertEquals(task.getId(), taskById.getId());
        assertEquals(task.getName(), taskById.getName());
        assertEquals(task.getDescription(), taskById.getDescription());
        assertEquals(task.getPriority(), taskById.getPriority());
        assertEquals(task.getStatus().getStatus(), taskById.getStatus());
        assertEquals(task.getManager().getNickname(), taskById.getAssignedManager());
    }

    @Test
    void getTaskTest_nonExistingTaskThrowsException() throws Exception {
        Long nonExistingTaskId = 1L;

        when(taskRepo.findById(nonExistingTaskId)).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> taskService.getTask(nonExistingTaskId));
        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void getAllTasksByProjectIdTest_success() throws Exception {
        Manager manager = new Manager();
        manager.setFirstName("Test");
        manager.setLastName("Test");
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setManager(manager);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));
        when(taskRepo.findByProject(any())).thenReturn(List.of(new Task(), new Task()));

        List<TaskDTO> tasks = taskService.getAllTasksByProjectId(project.getId());
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
    }

    @Test
    void getAllTasksByProjectIdTest_nonExistingProjectThrowsException() throws Exception {
        Long nonExistingProjectId = 1L;

        Manager manager = new Manager();
        manager.setFirstName("Test");
        manager.setLastName("Test");
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");
        manager.setId(1L);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> taskService.getAllTasksByProjectId(nonExistingProjectId));
        assertEquals("Project not found", exception.getMessage());
    }

    @Test
    void getAllTasksByUserIdTest_success() throws Exception {
        Manager manager = new Manager();
        manager.setFirstName("Test");
        manager.setLastName("Test");
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");
        manager.setId(1L);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(manager));
        when(taskRepo.findByUser(any())).thenReturn(List.of(new Task(), new Task()));

        List<TaskDTO> tasks = taskService.getAllTasksByUserId(manager.getId());

        assertNotNull(tasks);
        assertEquals(2, tasks.size());
    }

    @Test
    void getAllTasksByUserId_nonExistingUserThrowsException() throws Exception {
        Long nonExistingUserId = 1L;

        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> taskService.getAllTasksByUserId(nonExistingUserId));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getAllTasksByProjectIdAndUserIdTest_success() throws Exception {
        Manager manager = new Manager();
        manager.setFirstName("Test");
        manager.setLastName("Test");
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");
        manager.setId(1L);

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setManager(manager);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));
        when(taskRepo.findByProjectAndUser(any(), any())).thenReturn(List.of(new Task(), new Task()));

        List<TaskDTO> tasks = taskService.getAllTasksByProjectIdAndUserId(project.getId(), manager.getId());

        assertNotNull(tasks);
        assertEquals(2, tasks.size());
    }

    @Test
    void getAllTasksByProjectIdAndUserIdTest_nonExistingUserThrowsException() throws Exception {
        Long projectId = 1L;
        Long nonExistingUserId = 1L;

        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> taskService.getAllTasksByProjectIdAndUserId(projectId, nonExistingUserId));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getAllTasksByProjectIdAndUserIdTest_nonExistingProjectThrowsException() throws Exception {
        Long nonExistingProjectId = 1L;
        Long userId = 1L;

        Manager manager = new Manager();
        manager.setFirstName("Test");
        manager.setLastName("Test");
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");
        manager.setId(1L);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> taskService.getAllTasksByProjectIdAndUserId(nonExistingProjectId, userId));
        assertEquals("Project not found", exception.getMessage());
    }

    @Test
    void deleteTaskTest_success() throws Exception {
        Manager manager = new Manager();
        manager.setFirstName("Test");
        manager.setLastName("Test");
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Task testTask = new Task();
        testTask.setId(1L);
        testTask.setDescription("description");
        testTask.setPriority(2);
        testTask.setStatus(TaskStatus.IN_PROGRESS);
        testTask.setManager(manager);

        manager.getTasks().add(testTask);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(taskRepo.findById(anyLong())).thenReturn(Optional.of(testTask));
        doNothing().when(taskRepo).delete(any());

        TaskDTO task = taskService.deleteTask(testTask.getId());

        assertNotNull(task);
        assertEquals(testTask.getId(), task.getId());
        assertEquals("description", task.getDescription());
        assertEquals(testTask.getStatus().getStatus(), task.getStatus());
        assertEquals(testTask.getPriority(), task.getPriority());
    }

    @Test
    void deleteTaskTest_nonExistingTaskThrowsException() throws Exception {
        Manager manager = new Manager();
        manager.setFirstName("Test");
        manager.setLastName("Test");
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(taskRepo.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> taskService.deleteTask(1L));
        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void assignUserToTaskTest_success() throws Exception {
        Manager manager = new Manager();
        manager.setFirstName("Test");
        manager.setLastName("Test");
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Task testTask = new Task();
        testTask.setId(1L);
        testTask.setDescription("description");
        testTask.setPriority(2);
        testTask.setStatus(TaskStatus.IN_PROGRESS);
        testTask.setManager(manager);

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setNickname("Test Employee");
        employee.setEmail("test.employee@example.com");

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(taskRepo.findById(anyLong())).thenReturn(Optional.of(testTask));
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(employee));

        boolean assignUserToTask = taskService.assignUserToTask(testTask.getId(), employee.getId());

        assertTrue(assignUserToTask);
        assertTrue(employee.getTasks().contains(testTask));
        assertEquals(employee, testTask.getEmployee());
    }

    @Test
    void assignUserToTaskTest_nonExistingTaskThrowsException() throws Exception {
        Manager manager = new Manager();
        manager.setFirstName("Test");
        manager.setLastName("Test");
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(taskRepo.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> taskService.assignUserToTask(1L, 1L));
        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void assignUserToTaskTest_nonExistingEmployeeThrowsException() throws Exception {
        Manager manager = new Manager();
        manager.setFirstName("Test");
        manager.setLastName("Test");
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Task testTask = new Task();
        testTask.setId(1L);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(taskRepo.findById(anyLong())).thenReturn(Optional.of(testTask));
        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> taskService.assignUserToTask(1L, 1L));
        assertEquals("Employee not found", exception.getMessage());
    }

    @Test
    void assignUserToTaskTest_employeeAssignedToTaskThrowsException() throws Exception {
        Manager manager = new Manager();
        manager.setFirstName("Test");
        manager.setLastName("Test");
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setNickname("Test Employee");
        employee.setEmail("test.employee@example.com");

        Task testTask = new Task();
        testTask.setId(1L);
        testTask.setDescription("description");
        testTask.setPriority(2);
        testTask.setStatus(TaskStatus.IN_PROGRESS);
        testTask.setManager(manager);

        employee.getTasks().add(testTask);
        testTask.setEmployee(employee);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(taskRepo.findById(anyLong())).thenReturn(Optional.of(testTask));
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(employee));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> taskService.assignUserToTask(testTask.getId(), employee.getId()));
        assertEquals("Employee already assigned to task", exception.getMessage());
    }

    @Test
    void removeUserFromTaskTest_success() throws Exception {
        Manager manager = new Manager();
        manager.setFirstName("Test");
        manager.setLastName("Test");
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Task testTask = new Task();
        testTask.setId(1L);
        testTask.setDescription("description");
        testTask.setPriority(2);
        testTask.setStatus(TaskStatus.IN_PROGRESS);
        testTask.setManager(manager);

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setNickname("Test Employee");
        employee.setEmail("test.employee@example.com");

        employee.getTasks().add(testTask);
        testTask.setEmployee(employee);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(taskRepo.findById(anyLong())).thenReturn(Optional.of(testTask));
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(employee));

        boolean removeUserFromTask = taskService.removeUserFromTask(testTask.getId(), employee.getId());

        assertTrue(removeUserFromTask);
        assertTrue(employee.getTasks().isEmpty());
        assertNull(testTask.getEmployee());
    }

    @Test
    void removeUserFromTaskTest_nonExistingTaskThrowsException() throws Exception {
        Manager manager = new Manager();
        manager.setFirstName("Test");
        manager.setLastName("Test");
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(taskRepo.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> taskService.removeUserFromTask(1L, 1L));
        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void removeUserFromTaskTest_nonExistingEmployeeThrowsException() throws Exception {
        Manager manager = new Manager();
        manager.setFirstName("Test");
        manager.setLastName("Test");
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Task testTask = new Task();
        testTask.setId(1L);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(taskRepo.findById(anyLong())).thenReturn(Optional.of(testTask));
        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> taskService.removeUserFromTask(1L, 1L));
        assertEquals("Employee not found", exception.getMessage());
    }

    @Test
    void removeUserFromTaskTest_employeeNotAssignedToTaskThrowsException() throws Exception {
        Manager manager = new Manager();
        manager.setFirstName("Test");
        manager.setLastName("Test");
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setNickname("Test Employee");
        employee.setEmail("test.employee@example.com");

        Task testTask = new Task();
        testTask.setId(1L);
        testTask.setDescription("description");
        testTask.setPriority(2);
        testTask.setStatus(TaskStatus.IN_PROGRESS);
        testTask.setManager(manager);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(taskRepo.findById(anyLong())).thenReturn(Optional.of(testTask));
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(employee));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> taskService.removeUserFromTask(testTask.getId(), employee.getId()));
        assertEquals("Employee is not assigned to task", exception.getMessage());
    }
}