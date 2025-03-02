package ua.ellka.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProjectServiceImplTest {
    private ProjectService projectService;
    private ProjectRepo projectRepo;
    private UserRepo userRepo;

    @BeforeEach
    public void setUp() {
        projectRepo = mock(ProjectRepo.class);
        userRepo = mock(UserRepo.class);
        ProjectMapper projectMapper = ProjectMapper.INSTANCE;

        projectService = new ProjectServiceImpl(projectRepo, projectMapper, userRepo);
    }

    @Test
    void createProjectTest_success() throws ProjectTrackerPersistingException {
        ProjectDTO testProjectDTO = new ProjectDTO();
        testProjectDTO.setId(1L);
        testProjectDTO.setName("Test Project");
        testProjectDTO.setDescription("Test Description");
        testProjectDTO.setStatus("In Progress");
        testProjectDTO.setManagerName("Test Manager");

        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");

        Project mockProject = new Project();
        mockProject.setId(1L);
        mockProject.setName(testProjectDTO.getName());
        mockProject.setDescription(testProjectDTO.getDescription());
        mockProject.setStatus(ProjectStatus.fromString(testProjectDTO.getStatus()));
        mockProject.setManager(manager);

        when(userRepo.findByNickname(anyString())).thenReturn(Optional.of(manager));
        when(projectRepo.save(any())).thenReturn(Optional.of(mockProject));

        ProjectDTO createdProject = projectService.createProject(testProjectDTO);
        assertNotNull(createdProject);
        assertNotNull(testProjectDTO.getId());
    }

    @Test
    void createProjectTest_existingProjectExisting() throws ProjectTrackerPersistingException {
        ProjectDTO testProjectDTO = new ProjectDTO();
        testProjectDTO.setId(1L);
        testProjectDTO.setName("Test Project");
        testProjectDTO.setDescription("Test Description");
        testProjectDTO.setStatus("In Progress");
        testProjectDTO.setManagerName("Test Manager");

        Manager manager = new Manager();
        manager.setId(2L);
        manager.setNickname("Test Manager");

        Project mockProject = new Project();
        mockProject.setId(1L);
        mockProject.setName(testProjectDTO.getName());
        mockProject.setDescription(testProjectDTO.getDescription());
        mockProject.setStatus(ProjectStatus.fromString(testProjectDTO.getStatus()));

        mockProject.setManager(manager);
        manager.getProjects().add(mockProject);

        when(userRepo.findByNickname(anyString())).thenReturn(Optional.of(manager));
        when(projectRepo.find(anyLong())).thenReturn(Optional.of(mockProject));
        when(projectRepo.save(any())).thenReturn(Optional.of(mockProject));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> projectService.createProject(testProjectDTO));
        assertEquals("Project already exists", exception.getMessage());
    }

    @Test
    void updateProjectTest_success() throws ProjectTrackerPersistingException {
        Project mockProject = new Project();
        mockProject.setId(1L);
        mockProject.setName("Test Project");
        mockProject.setDescription("Test Description");
        mockProject.setStatus(ProjectStatus.IN_PROGRESS);

        ProjectDTO testProjectDTO = new ProjectDTO();
        testProjectDTO.setId(9999L);
        testProjectDTO.setName("Test New Project");
        testProjectDTO.setDescription("Test New Description");
        testProjectDTO.setStatus("In Progress");
        testProjectDTO.setManagerName("Test Manager");

        when(projectRepo.find(anyLong())).thenReturn(Optional.of(mockProject));
        when(projectRepo.update(any())).thenReturn(Optional.of(mockProject));

        ProjectDTO updatedProject = projectService.updateProject(1L, testProjectDTO);

        assertNotNull(updatedProject);
        assertEquals(1L, updatedProject.getId());
        assertEquals(testProjectDTO.getName(), updatedProject.getName());
        assertEquals(testProjectDTO.getDescription(), updatedProject.getDescription());
        assertEquals(testProjectDTO.getStatus(), updatedProject.getStatus());
    }

    @Test
    void updateProjectTest_nonExistingProjectThrowsException() throws ProjectTrackerPersistingException {
        when(projectRepo.find(anyLong())).thenReturn(Optional.empty());

        ProjectDTO testProjectDTO = new ProjectDTO();
        testProjectDTO.setId(1000L);
        testProjectDTO.setName("Test Project");
        testProjectDTO.setDescription("Test Description");
        testProjectDTO.setStatus("In Progress");

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> projectService.updateProject(1L, testProjectDTO));
        assertEquals("Project not found", exception.getMessage());
    }

    @Test
    void deleteProjectTest_success() throws ProjectTrackerPersistingException {
        Project testProject = new Project();
        testProject.setId(1L);
        testProject.setName("Test Project");
        testProject.setDescription("Test Description");
        testProject.setStatus(ProjectStatus.IN_PROGRESS);

        when(projectRepo.find(anyLong())).thenReturn(Optional.of(testProject));
        when(projectRepo.delete(any())).thenReturn(Optional.of(testProject));

        ProjectDTO deletedProject = projectService.deleteProject(testProject.getId());

        assertNotNull(deletedProject);
        assertEquals(testProject.getId(), deletedProject.getId());
        assertEquals(testProject.getName(), deletedProject.getName());
        assertEquals(testProject.getDescription(), deletedProject.getDescription());
        assertEquals(testProject.getStatus().getStatus(), deletedProject.getStatus());
    }

    @Test
    void deleteProjectTest_nonExistingProjectThrowsException() throws ProjectTrackerPersistingException {
        when(projectRepo.find(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> projectService.deleteProject(1L));
        assertEquals("Project not found", exception.getMessage());
    }

    @Test
    void getProjectsByUserId_returnsProject() throws ProjectTrackerPersistingException {
        Long userId = 1L;
        when(userRepo.find(anyLong())).thenReturn(Optional.of(new Employee()));
        when(projectRepo.findByUser(any())).thenReturn(List.of(new Project()));

        List<ProjectDTO> projects = projectService.getAllProjectsByUserId(userId);

        assertNotNull(projects);
        assertFalse(projects.isEmpty());
    }

    @Test
    void getProjectsByUserId_nonExistingUserThrowsException() throws ProjectTrackerPersistingException {
        Long userId = 999L;
        when(userRepo.find(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> projectService.getAllProjectsByUserId(userId));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getProject_returnsProject() throws ProjectTrackerPersistingException {
        Long projectId = 1L;
        when(projectRepo.find(anyLong())).thenReturn(Optional.of(new Project()));

        ProjectDTO project = projectService.getProject(projectId);

        assertNotNull(project);
    }

    @Test
    void getProject_nonExistingProjectThrowsException() throws ProjectTrackerPersistingException {
        Long projectId = 999L;
        when(projectRepo.find(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> projectService.getProject(projectId));
        assertEquals("Project not found", exception.getMessage());
    }

    @Test
    void assignUserToProject_success() throws ProjectTrackerPersistingException {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setNickname("Test Employee");

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStatus(ProjectStatus.IN_PROGRESS);

        when(projectRepo.find(anyLong())).thenReturn(Optional.of(project));
        when(userRepo.find(anyLong())).thenReturn(Optional.of(employee));

        boolean assignUserToProject = projectService.assignUserToProject(project.getId(), employee.getId());

        assertTrue(assignUserToProject);
    }

    @Test
    void assignUserToProject_nonExistingProjectThrowsException() throws ProjectTrackerPersistingException {
        when(projectRepo.find(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> projectService.assignUserToProject(1L, 999L));
        assertEquals("Project not found", exception.getMessage());
    }

    @Test
    void assignUserToProjectTest_nonExistingEmployeeThrowsException() throws ProjectTrackerPersistingException {
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStatus(ProjectStatus.IN_PROGRESS);


        when(projectRepo.find(anyLong())).thenReturn(Optional.of(project));
        when(userRepo.find(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> projectService.assignUserToProject(project.getId(), 1L));
        assertEquals("Employee not found", exception.getMessage());
    }

    @Test
    void assignUserToProjectTest_userIsNotEmployeeThrowsException() throws ProjectTrackerPersistingException {
        User nonEmployee = new Manager();
        nonEmployee.setId(1L);
        nonEmployee.setNickname("Test NonEmployee");

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStatus(ProjectStatus.IN_PROGRESS);

        when(projectRepo.find(anyLong())).thenReturn(Optional.of(project));
        when(userRepo.find(anyLong())).thenReturn(Optional.of(nonEmployee));

        assertThrows(ClassCastException.class,
                () -> projectService.assignUserToProject(project.getId(), nonEmployee.getId()));
    }

    @Test
    void assignUserToProjectTest_employeeIsAssignToProjectThrowsException() throws ProjectTrackerPersistingException {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setNickname("Test Employee");

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStatus(ProjectStatus.IN_PROGRESS);

        employee.getProjects().add(project);
        project.getEmployees().add(employee);

        when(projectRepo.find(anyLong())).thenReturn(Optional.of(project));
        when(userRepo.find(anyLong())).thenReturn(Optional.of(employee));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> projectService.assignUserToProject(project.getId(), employee.getId()));
        assertEquals("User is already assigned to this project", exception.getMessage());
    }

    @Test
    void removeUserFromProject_success() throws ProjectTrackerPersistingException {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setNickname("Test Employee");

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStatus(ProjectStatus.IN_PROGRESS);

        employee.getProjects().add(project);
        project.getEmployees().add(employee);

        when(projectRepo.find(anyLong())).thenReturn(Optional.of(project));
        when(userRepo.find(anyLong())).thenReturn(Optional.of(employee));

        boolean removed = projectService.removeUserFromProject(project.getId(), employee.getId());

        assertTrue(removed);
    }

    @Test
    void removeUserFromProject_nonExistingProjectThrowsException() throws ProjectTrackerPersistingException {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setNickname("Test Employee");

        when(projectRepo.find(anyLong())).thenReturn(Optional.empty());
        when(userRepo.find(anyLong())).thenReturn(Optional.of(employee));

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> projectService.removeUserFromProject(1L, employee.getId()));
        assertEquals("Project not found", exception.getMessage());
    }

    @Test
    void removeUserFromProject_nonExistingEmployeeThrowsException() throws ProjectTrackerPersistingException {
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStatus(ProjectStatus.IN_PROGRESS);

        when(projectRepo.find(anyLong())).thenReturn(Optional.of(project));
        when(userRepo.find(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> projectService.removeUserFromProject(project.getId(), 1L));
        assertEquals("Employee not found", exception.getMessage());
    }

    @Test
    void removeUserFromProject_employeeIsNotAssignToProjectThrowsException() throws ProjectTrackerPersistingException {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setNickname("Test Employee");;

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStatus(ProjectStatus.IN_PROGRESS);

        when(projectRepo.find(anyLong())).thenReturn(Optional.of(project));
        when(userRepo.find(anyLong())).thenReturn(Optional.of(employee));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> projectService.removeUserFromProject(project.getId(), employee.getId()));
        assertEquals("User is not assigned to this project", exception.getMessage());
    }

    @Test
    void removeUserFromProject_userIsNotEmployeeThrowsException() throws ProjectTrackerPersistingException {
        User employee = new Manager();
        employee.setId(1L);
        employee.setNickname("Test Employee");

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStatus(ProjectStatus.IN_PROGRESS);


        when(projectRepo.find(anyLong())).thenReturn(Optional.of(project));
        when(userRepo.find(anyLong())).thenReturn(Optional.of(employee));

        assertThrows(ClassCastException.class,
                () -> projectService.removeUserFromProject(project.getId(), employee.getId()));
    }
}
