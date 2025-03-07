package ua.ellka.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ua.ellka.dto.ProjectDTO;
import ua.ellka.exception.NotFoundServiceException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ProjectServiceImplTest {
    private ProjectService projectService;
    private ProjectRepo projectRepo;
    private UserRepo userRepo;
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        projectRepo = mock(ProjectRepo.class);
        userRepo = mock(UserRepo.class);
        ProjectMapper projectMapper = ProjectMapper.INSTANCE;
        authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        projectService = new ProjectServiceImpl(projectRepo, projectMapper, userRepo);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createProjectTest_success() throws Exception {
        ProjectDTO testProjectDTO = new ProjectDTO();
        testProjectDTO.setId(1L);
        testProjectDTO.setName("Test Project");
        testProjectDTO.setDescription("Test Description");
        testProjectDTO.setStatus("In Progress");
        testProjectDTO.setManagerName("Test Manager");

        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Project mockProject = new Project();
        mockProject.setId(1L);
        mockProject.setName(testProjectDTO.getName());
        mockProject.setDescription(testProjectDTO.getDescription());
        mockProject.setStatus(ProjectStatus.fromString(testProjectDTO.getStatus()));
        mockProject.setManager(manager);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(userRepo.findByNickname(anyString())).thenReturn(Optional.of(manager));
        when(projectRepo.save(any())).thenReturn(mockProject);

        ProjectDTO createdProject = projectService.createProject(testProjectDTO);
        assertNotNull(createdProject);
        assertNotNull(testProjectDTO.getId());
    }

    @Test
    void createProjectTest_existingProjectExisting() throws Exception {
        ProjectDTO testProjectDTO = new ProjectDTO();
        testProjectDTO.setId(1L);
        testProjectDTO.setName("Test Project");
        testProjectDTO.setDescription("Test Description");
        testProjectDTO.setStatus("In Progress");
        testProjectDTO.setManagerName("Test Manager");

        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Project mockProject = new Project();
        mockProject.setId(1L);
        mockProject.setName(testProjectDTO.getName());
        mockProject.setDescription(testProjectDTO.getDescription());
        mockProject.setStatus(ProjectStatus.fromString(testProjectDTO.getStatus()));

        mockProject.setManager(manager);
        manager.getProjects().add(mockProject);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(userRepo.findByNickname(anyString())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(mockProject));
        when(projectRepo.save(any())).thenReturn(mockProject);

        ServiceException exception = assertThrows(ServiceException.class,
                () -> projectService.createProject(testProjectDTO));
        assertEquals("Project already exists", exception.getMessage());
    }

    @Test
    void updateProjectTest_success() throws Exception {
        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Project mockProject = new Project();
        mockProject.setId(1L);
        mockProject.setName("Test Project");
        mockProject.setDescription("Test Description");
        mockProject.setStatus(ProjectStatus.IN_PROGRESS);
        mockProject.setManager(manager);

        ProjectDTO testProjectDTO = new ProjectDTO();
        testProjectDTO.setId(9999L);
        testProjectDTO.setName("Test New Project");
        testProjectDTO.setDescription("Test New Description");
        testProjectDTO.setStatus("In Progress");
        testProjectDTO.setManagerName("Test Manager");

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(mockProject));
        when(projectRepo.save(any())).thenReturn(mockProject);

        ProjectDTO updatedProject = projectService.updateProject(1L, testProjectDTO);

        assertNotNull(updatedProject);
        assertEquals(1L, updatedProject.getId());
        assertEquals(testProjectDTO.getName(), updatedProject.getName());
        assertEquals(testProjectDTO.getDescription(), updatedProject.getDescription());
        assertEquals(testProjectDTO.getStatus(), updatedProject.getStatus());
    }

    @Test
    void updateProjectTest_nonExistingProjectThrowsException() throws Exception {
        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.empty());

        ProjectDTO testProjectDTO = new ProjectDTO();
        testProjectDTO.setId(1000L);
        testProjectDTO.setName("Test Project");
        testProjectDTO.setDescription("Test Description");
        testProjectDTO.setStatus("In Progress");
        testProjectDTO.setManagerName("Test Manager");

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> projectService.updateProject(1L, testProjectDTO));
        assertEquals("Project not found", exception.getMessage());
    }

    @Test
    void deleteProjectTest_success() throws Exception {
        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Project testProject = new Project();
        testProject.setId(1L);
        testProject.setName("Test Project");
        testProject.setDescription("Test Description");
        testProject.setStatus(ProjectStatus.IN_PROGRESS);
        testProject.setManager(manager);

        manager.getProjects().add(testProject);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(testProject));
        doNothing().when(projectRepo).delete(testProject);

        ProjectDTO deletedProject = projectService.deleteProject(testProject.getId());

        assertNotNull(deletedProject);
        assertEquals(testProject.getId(), deletedProject.getId());
        assertEquals(testProject.getName(), deletedProject.getName());
        assertEquals(testProject.getDescription(), deletedProject.getDescription());
        assertEquals(testProject.getStatus().getStatus(), deletedProject.getStatus());
    }

    @Test
    void deleteProjectTest_nonExistingProjectThrowsException() throws Exception {
        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> projectService.deleteProject(1L));
        assertEquals("Project not found", exception.getMessage());
    }

    @Test
    void getProjectsByUserId_returnsProject() throws Exception {
        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(manager));
        when(projectRepo.findByUser(any())).thenReturn(List.of(new Project()));

        List<ProjectDTO> projects = projectService.getAllProjectsByUserId(manager.getId());

        assertNotNull(projects);
        assertFalse(projects.isEmpty());
    }

    @Test
    void getProjectsByUserId_nonExistingUserThrowsException() throws Exception {
        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> projectService.getAllProjectsByUserId(1L));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getProject_returnsProject() throws Exception {
        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Project project = new Project();
        project.setId(1L);
        project.setManager(manager);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));

        ProjectDTO projectDTO = projectService.getProject(project.getId());

        assertNotNull(projectDTO);
    }

    @Test
    void getProject_nonExistingProjectThrowsException() throws Exception {
        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> projectService.getProject(1L));
        assertEquals("Project not found", exception.getMessage());
    }

    @Test
    void assignUserToProject_success() throws Exception {
        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setNickname("Test Employee");
        employee.setEmail("test.employee@example.com");

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setManager(manager);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(employee));

        boolean assignUserToProject = projectService.assignUserToProject(project.getId(), employee.getId());

        assertTrue(assignUserToProject);
    }

    @Test
    void assignUserToProject_nonExistingProjectThrowsException() throws Exception {
        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> projectService.assignUserToProject(1L, 1L));
        assertEquals("Project not found", exception.getMessage());
    }

    @Test
    void assignUserToProjectTest_nonExistingEmployeeThrowsException() throws Exception {
        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setManager(manager);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));
        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> projectService.assignUserToProject(project.getId(), 1L));
        assertEquals("Employee not found", exception.getMessage());
    }


    @Test
    void assignUserToProjectTest_userIsNotEmployeeThrowsException() throws Exception {
        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        User nonEmployee = new Manager();
        nonEmployee.setId(1L);
        nonEmployee.setNickname("Test NonEmployee");
        nonEmployee.setEmail("test.nonemployee@example.com");

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setManager(manager);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(nonEmployee));

        assertThrows(ServiceException.class,
                () -> projectService.assignUserToProject(project.getId(), nonEmployee.getId()));
    }

    @Test
    void assignUserToProjectTest_employeeIsAssignToProjectThrowsException() throws Exception {
        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setNickname("Test Employee");
        employee.setEmail("test.employee@example.com");

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setManager(manager);

        employee.getProjects().add(project);
        project.getEmployees().add(employee);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(employee));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> projectService.assignUserToProject(project.getId(), employee.getId()));
        assertEquals("User is already assigned to this project", exception.getMessage());
    }

    @Test
    void removeUserFromProject_success() throws Exception {
        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setNickname("Test Employee");
        employee.setEmail("test.employee@example.com");

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setManager(manager);

        employee.getProjects().add(project);
        project.getEmployees().add(employee);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(employee));

        boolean removed = projectService.removeUserFromProject(project.getId(), employee.getId());

        assertTrue(removed);
    }

    @Test
    void removeUserFromProject_nonExistingProjectThrowsException() throws Exception {
        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setNickname("Test Employee");
        employee.setEmail("test.employee@example.com");

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(employee));

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> projectService.removeUserFromProject(1L, employee.getId()));
        assertEquals("Project not found", exception.getMessage());
    }

    @Test
    void removeUserFromProject_nonExistingEmployeeThrowsException() throws Exception {
        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setManager(manager);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));
        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> projectService.removeUserFromProject(project.getId(), 1L));
        assertEquals("Employee not found", exception.getMessage());
    }

    @Test
    void removeUserFromProject_employeeIsNotAssignToProjectThrowsException() throws Exception {
        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setNickname("Test Employee");
        employee.setEmail("test.employee@example.com");

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setManager(manager);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(employee));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> projectService.removeUserFromProject(project.getId(), employee.getId()));
        assertEquals("User is not assigned to this project", exception.getMessage());
    }

    @Test
    void removeUserFromProject_userIsNotEmployeeThrowsException() throws Exception {
        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");
        manager.setEmail("test.manager@example.com");

        User nonEmployee = new Manager();
        nonEmployee.setId(1L);
        nonEmployee.setNickname("Test NonEmployee");
        nonEmployee.setEmail("test.nonemployee@example.com");

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setManager(manager);

        when(authentication.getName()).thenReturn(manager.getEmail());
        when(userRepo.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(nonEmployee));

        assertThrows(ServiceException.class,
                () -> projectService.removeUserFromProject(project.getId(), nonEmployee.getId()));
    }
}
