package ua.ellka.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.model.project.Project;
import ua.ellka.model.project.ProjectStatus;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;
import ua.ellka.model.user.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectHibernateRepoTest extends RepoParent {
    private ProjectHibernateRepo projectHibernateRepo;

    @BeforeEach
    void setUp() {
        projectHibernateRepo = new ProjectHibernateRepo(sessionFactory);
    }

    @Test
    void findTest_success() throws ProjectTrackerPersistingException {
        Long projectId = 1L;
        Optional<Project> project = projectHibernateRepo.find(projectId);

        assertTrue(project.isPresent(), "Project not found");
        assertNotNull(project.get().getId());
        assertEquals(projectId, project.get().getId());
    }

    @Test
    void findTest_notFound() throws ProjectTrackerPersistingException {
        Long projectId = 999L;
        Optional<Project> project = projectHibernateRepo.find(projectId);

        assertTrue(project.isEmpty(), "Project should not exist");
    }

    @Test
    void findByNameTest_success() throws ProjectTrackerPersistingException {
        String name = "Project Alpha";
        Optional<Project> project = projectHibernateRepo.findByName(name);

        assertTrue(project.isPresent(), "Project not found by name");
        assertNotNull(project.get());
        assertNotNull(project.get().getName());
        assertEquals(name, project.get().getName());

    }

    @Test
    void findByNameTest_notFound() throws ProjectTrackerPersistingException {
        String nonExistentName = "NonExistent Project";
        Optional<Project> project = projectHibernateRepo.findByName(nonExistentName);

        assertTrue(project.isEmpty(), "Project should not exist");
    }

    @Test
    void saveTest_idNotNull() throws ProjectTrackerPersistingException {
        Project project = new Project();
        project.setName("Project El");
        project.setDescription("Project El Description");
        project.setPriority(1);
        project.setStatus(ProjectStatus.IN_PROGRESS);

        Optional<Project> saved = projectHibernateRepo.save(project);
        assertTrue(saved.isPresent(), "Project was not saved");

        Project savedProject = saved.get();

        assertNotNull(savedProject.getId());
        assertEquals("Project El", savedProject.getName());
        assertEquals("Project El Description", savedProject.getDescription());
        assertEquals(1, savedProject.getPriority());
        assertEquals(ProjectStatus.IN_PROGRESS, savedProject.getStatus());
    }


    @Test
    void deleteByProjectTest_success() throws ProjectTrackerPersistingException {
        Optional<Project> optionalProject = projectHibernateRepo.findByName("Project Alpha");
        assertTrue(optionalProject.isPresent(), "Project Alpha not found");

        Project project = optionalProject.get();
        Optional<Project> deleted = projectHibernateRepo.delete(project);

        assertTrue(deleted.isPresent(), "Deleted project not found");
        assertEquals(project.getId(), deleted.get().getId());

        Optional<Project> fromDb = projectHibernateRepo.findByName("Project Alpha");
        assertTrue(fromDb.isEmpty(), "Project Alpha was not deleted");
    }

    @Test
    void updateTest_success() throws ProjectTrackerPersistingException {
        Long projectId = 1L;
        Optional<Project> existingProjectOptional = projectHibernateRepo.find(projectId);
        assertTrue(existingProjectOptional.isPresent(), "Project to update not found");

        Project existingProject = existingProjectOptional.get();
        existingProject.setName("Updated Project Name");
        existingProject.setDescription("Updated Description");
        existingProject.setPriority(2);
        existingProject.setStatus(ProjectStatus.COMPLETED);

        Optional<Project> updatedProject = projectHibernateRepo.update(existingProject);
        assertTrue(updatedProject.isPresent(), "Project update failed");

        Project updated = updatedProject.get();
        assertEquals("Updated Project Name", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
        assertEquals(2, updated.getPriority());
        assertEquals(ProjectStatus.COMPLETED, updated.getStatus());
    }

    @Test
    void updateTest_emptyName() throws ProjectTrackerPersistingException {
        Long projectId = 1L;
        Optional<Project> existingProjectOptional = projectHibernateRepo.find(projectId);
        assertTrue(existingProjectOptional.isPresent(), "Project to update not found");

        Project existingProject = existingProjectOptional.get();
        existingProject.setName("");

        Optional<Project> updatedProject = projectHibernateRepo.update(existingProject);
        assertTrue(updatedProject.isPresent(), "Project should be updated even if name is null");

        Project updated = updatedProject.get();
        assertEquals("", updated.getName());
    }

    @Test
    void findProjectByUserTest_manager_success() throws ProjectTrackerPersistingException {
        User manager = new Manager();
        manager.setId(2L);

        List<Project> projects = projectHibernateRepo.findByUser(manager);

        assertNotNull(projects);
        assertFalse(projects.isEmpty(), "Projects list should not be empty");
        assertTrue(projects.stream()
                .allMatch(project -> project.getManager().getId().equals(manager.getId())));
    }

    @Test
    void findProjectByUserTest_employee_success() throws ProjectTrackerPersistingException {
        Employee employee = new Employee();
        employee.setId(4L);

        List<Project> projects = projectHibernateRepo.findByUser(employee);

        System.out.println(projects.size());

        assertNotNull(projects);
        assertFalse(projects.isEmpty(), "Projects list should not be empty");
        assertTrue(projects.stream()
                .allMatch(project -> project.getEmployees().stream()
                        .findFirst()
                        .map(employeeInProject -> employeeInProject.getId().equals(employee.getId()))
                        .orElse(false)
                )
        );
    }

    @Test
    void findProjectByUserTest_userNotFound() throws ProjectTrackerPersistingException {
        User manager = new Manager();
        manager.setId(999L);

        List<Project> projects = projectHibernateRepo.findByUser(manager);

        assertTrue(projects.isEmpty(), "Projects list should not be empty");
    }
}