package ua.ellka.mapper;

import ua.ellka.dto.EmployeeDTO;
import ua.ellka.dto.ManagerDTO;
import ua.ellka.dto.ProjectDTO;
import ua.ellka.dto.TaskDTO;
import ua.ellka.model.project.Project;
import ua.ellka.model.project.ProjectStatus;
import ua.ellka.model.task.Task;
import ua.ellka.model.task.TaskStatus;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TestData {
    public static final Long USER_ID = 1L;
    public static final String NICKNAME = "johnny";
    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Doe";
    public static final String EMAIL = "john.doe@example.com";
    public static final String PHONE_NUMBER = "123456789";
    public static final String PASSWORD = "password123";

    public static Employee createEmployee() {
        Employee employee = new Employee();
        employee.setId(USER_ID);
        employee.setNickname(NICKNAME);
        employee.setFirstName(FIRST_NAME);
        employee.setLastName(LAST_NAME);
        employee.setEmail(EMAIL);
        employee.setPhoneNumber(PHONE_NUMBER);
        employee.setPassword(PASSWORD);
        employee.setRegisteredAt(LocalDateTime.now());
        employee.setLastLoginAt(LocalDateTime.now());
        employee.setProjects(Set.of(new Project()));
        employee.setTasks(Set.of(new Task(), new Task(), new Task()));
        return employee;
    }

    public static EmployeeDTO createEmployeeDTO() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(USER_ID);
        employeeDTO.setNickname(NICKNAME);
        employeeDTO.setFirstName(FIRST_NAME);
        employeeDTO.setLastName(LAST_NAME);
        employeeDTO.setEmail(EMAIL);
        employeeDTO.setPhoneNumber(PHONE_NUMBER);
        employeeDTO.setProjectCount(1L);
        employeeDTO.setTaskCount(3L);
        return employeeDTO;
    }

    public static Manager createManager() {
        Manager manager = new Manager();
        manager.setId(USER_ID);
        manager.setNickname(NICKNAME);
        manager.setFirstName(FIRST_NAME);
        manager.setLastName(LAST_NAME);
        manager.setEmail(EMAIL);
        manager.setPhoneNumber(PHONE_NUMBER);
        manager.setPassword(PASSWORD);
        manager.setRegisteredAt(LocalDateTime.now());
        manager.setLastLoginAt(LocalDateTime.now());
        manager.setProjects(Set.of(new Project(), new Project(), new Project(), new Project()));
        return manager;
    }

    public static ManagerDTO createManagerDTO() {
        ManagerDTO managerDTO = new ManagerDTO();
        managerDTO.setNickname(NICKNAME);
        managerDTO.setFirstName(FIRST_NAME);
        managerDTO.setLastName(LAST_NAME);
        managerDTO.setEmail(EMAIL);
        managerDTO.setPhoneNumber(PHONE_NUMBER);
        managerDTO.setProjectCount(4L);
        return managerDTO;
    }

    public static final Employee EMPLOYEE = createEmployee();
    public static final EmployeeDTO EMPLOYEE_DTO = createEmployeeDTO();

    public static final Manager MANAGER = createManager();
    public static final ManagerDTO MANAGER_DTO = createManagerDTO();

    public static final Long PROJECT_ID = 2L;
    public static final String PROJECT_NAME = "Project Alpha";
    public static final String PROJECT_DESCRIPTION = "A sample project for testing";
    public static final int PROJECT_PRIORITY = 5;
    public static final ProjectStatus PROJECT_STATUS = ProjectStatus.IN_PROGRESS;

    public static final Project  PROJECT = new Project(
            PROJECT_ID,
            PROJECT_NAME,
            PROJECT_DESCRIPTION,
            PROJECT_PRIORITY,
            PROJECT_STATUS,
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDate.now(),
            LocalDate.now().plusDays(10),
            MANAGER,
            new HashSet<>(),
            new HashSet<>()
    );
    public static final ProjectDTO PROJECT_DTO = new ProjectDTO(
            PROJECT_ID,
            PROJECT_NAME,
            PROJECT_DESCRIPTION,
            PROJECT_PRIORITY,
            PROJECT_STATUS.getStatus(),
            LocalDate.now().atStartOfDay(),
            MANAGER_DTO.getNickname()
    );

    public static final Long TASK_ID = 3L;
    public static final String TASK_NAME = "Task 1";
    public static final String TASK_DESCRIPTION = "Sample task description";
    public static final TaskStatus TASK_STATUS = TaskStatus.TODO;
    public static final String TASK_TYPE = "Development";
    public static final int TASK_PRIORITY = 1;

    public static final Task TASK = new Task(
            TASK_ID,
            TASK_NAME,
            TASK_DESCRIPTION,
            TASK_STATUS,
            null,
            TASK_STATUS,
            TASK_TYPE,
            TASK_PRIORITY,
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDate.now(),
            LocalDate.now().plusDays(5),
            new ArrayList<>(),
            new HashSet<>(),
            EMPLOYEE,
            MANAGER,
            PROJECT
    );
    public static final TaskDTO TASK_DTO = new TaskDTO(
            TASK_ID,
            TASK_NAME,
            TASK_DESCRIPTION,
            TASK_STATUS.getStatus(),
            TASK_TYPE,
            TASK_PRIORITY,
            LocalDateTime.now(),
            LocalDate.now().plusDays(5),
            MANAGER_DTO.getNickname(),
            EMPLOYEE_DTO.getNickname(),
            PROJECT_NAME
    );
}
