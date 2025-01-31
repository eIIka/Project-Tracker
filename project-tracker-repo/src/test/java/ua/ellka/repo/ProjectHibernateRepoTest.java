package ua.ellka.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.model.project.Project;
import ua.ellka.model.project.ProjectStatus;

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
    void deleteTest_success() throws ProjectTrackerPersistingException {
        Long projectId = 1L;

        Optional<Project> project = projectHibernateRepo.find(projectId);
        assertTrue(project.isPresent(), "Project to be deleted not found");

        Optional<Project> deleted = projectHibernateRepo.delete(projectId);
        assertTrue(deleted.isPresent(), "Deleted project not found");
        assertEquals(projectId, deleted.get().getId());

        Optional<Project> fromDb = projectHibernateRepo.find(projectId);
        assertTrue(fromDb.isEmpty(), "Project was not deleted");
    }

    @Test
    void deleteTest_notFound() throws ProjectTrackerPersistingException {
        Long nonExistentId = 999L;
        Optional<Project> deleted = projectHibernateRepo.delete(nonExistentId);

        assertTrue(deleted.isEmpty(), "No project should be deleted");
    }

    @Test
    void deleteByProjectTest_success() throws ProjectTrackerPersistingException {
        Optional<Project> optionalProject = projectHibernateRepo.findByName("Project Alpha");
        assertTrue(optionalProject.isPresent(), "Project Alpha not found");

        Project project = optionalProject.get();
        Optional<Project> deleted = projectHibernateRepo.deleteByProject(project);

        assertTrue(deleted.isPresent(), "Deleted project not found");
        assertEquals(project.getId(), deleted.get().getId());

        Optional<Project> fromDb = projectHibernateRepo.findByName("Project Alpha");
        assertTrue(fromDb.isEmpty(), "Project Alpha was not deleted");
    }

    @Test
    void deleteByProjectTest_notFound() throws ProjectTrackerPersistingException {
        Project project = new Project();
        project.setId(999L);

        Optional<Project> deleted = projectHibernateRepo.deleteByProject(project);

        assertTrue(deleted.isEmpty(), "No project should be deleted");
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
    void updateTest_notFound() throws ProjectTrackerPersistingException {
        Project nonExistentProject = new Project();
        nonExistentProject.setId(999L);

        Optional<Project> updatedProject = projectHibernateRepo.update(nonExistentProject);
        assertTrue(updatedProject.isEmpty(), "Non-existent project should not be updated");
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
    void updateTest_nullName() throws ProjectTrackerPersistingException {
        Long projectId = 1L;
        Optional<Project> existingProjectOptional = projectHibernateRepo.find(projectId);
        assertTrue(existingProjectOptional.isPresent(), "Project to update not found");

        Project existingProject = existingProjectOptional.get();
        existingProject.setName(null);

        Optional<Project> updatedProject = projectHibernateRepo.update(existingProject);
        assertTrue(updatedProject.isPresent(), "Project should be updated even if name is null");

        Project updated = updatedProject.get();
        assertNotNull(updated.getName(),"Name should not be null");
    }
}