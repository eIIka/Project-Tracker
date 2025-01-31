package ua.ellka.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.model.project.Project;
import ua.ellka.model.project.ProjectStatus;
import ua.ellka.model.task.Task;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TaskHibernateRepoTest extends RepoParent{
    private TaskHibernateRepo taskHibernateRepo;

    @BeforeEach
    void setUp() {
        taskHibernateRepo = new TaskHibernateRepo(sessionFactory);
    }

    @Test
    void findTest_success() throws ProjectTrackerPersistingException {
        Long taskId = 1L;
        Optional<Task> task = taskHibernateRepo.find(taskId);

        assertTrue(task.isPresent(), "Task not found");
        assertNotNull(task.get().getId());
        assertEquals(taskId, task.get().getId());
    }

    @Test
    void findTest_notFound() throws ProjectTrackerPersistingException {
        Long taskId = 999L;
        Optional<Task> task = taskHibernateRepo.find(taskId);

        assertTrue(task.isEmpty(), "Task should not exist");
    }

    @Test
    void findByNameTest_success() throws ProjectTrackerPersistingException {
        String name = "Task 1";
        Optional<Task> task = taskHibernateRepo.findByName(name);

        assertTrue(task.isPresent(), "Task not found by name");
        assertNotNull(task.get().getId());
        assertNotNull(task.get().getName());
        assertEquals(name, task.get().getName());

    }

    @Test
    void findByNameTest_notFound() throws ProjectTrackerPersistingException {
        String name = "not found";
        Optional<Task> task = taskHibernateRepo.findByName(name);

        assertTrue(task.isEmpty(), "Task should not exist");
    }

    @Test
    void saveTest_idNotNull() throws ProjectTrackerPersistingException {
        Task task = new Task();
        task.setName("Task 1");
        task.setDescription("Task 1");
        task.setPriority(2);

        Optional<Task> taskOptional = taskHibernateRepo.save(task);
        assertTrue(taskOptional.isPresent(), "Task was not saved");

        Task taskSaved = taskOptional.get();

        assertNotNull(taskSaved.getId());
        assertEquals("Task 1", taskSaved.getName());
        assertEquals("Task 1", taskSaved.getDescription());
        assertEquals(2, taskSaved.getPriority());
    }

    @Test
    void deleteTest_success() throws ProjectTrackerPersistingException {
        Optional<Task> optionalTask = taskHibernateRepo.find(1L);
        assertTrue(optionalTask.isPresent(), "Task not found");

        Task task = optionalTask.get();
        Optional<Task> deleted = taskHibernateRepo.delete(task);

        assertTrue(deleted.isPresent(), "Deleted task not found");
        assertEquals(task.getId(), deleted.get().getId());

        Optional<Task> fromDb = taskHibernateRepo.find(1L);
        assertTrue(fromDb.isEmpty(), "Task was not deleted");
    }

    @Test
    void deleteTest_notFound() throws ProjectTrackerPersistingException {
        Task task = new Task();
        task.setId(999L);

        Optional<Task> deleted = taskHibernateRepo.delete(task);

        assertTrue(deleted.isEmpty(), "No task should be deleted");
    }

    @Test
    void findAllByProjectTest_success() throws ProjectTrackerPersistingException {
        Long projectId = 1L;
        List<Task> tasks = taskHibernateRepo.findAllByProjectId(projectId);

        assertNotNull(tasks, "Tasks list should not be null");
        assertFalse(tasks.isEmpty(), "Tasks list should not be empty");
        assertTrue(tasks.stream().allMatch(task -> task.getProject().getId().equals(projectId)), "All tasks should be found");
    }

    @Test
    void findAllByProjectTest_notTasks() throws ProjectTrackerPersistingException {
        Long projectId = 6L;
        List<Task> tasks = taskHibernateRepo.findAllByProjectId(projectId);

        assertNotNull(tasks, "Tasks list should not be null");
        assertTrue(tasks.isEmpty(), "Tasks list should not be empty");
    }

    @Test
    void findAllByProjectTest_notFound() throws ProjectTrackerPersistingException {
        Long projectId = 999L;
        List<Task> tasks = taskHibernateRepo.findAllByProjectId(projectId);

        assertNotNull(tasks, "Tasks list should not be null");
        assertTrue(tasks.isEmpty(), "Tasks list should be empty for a non-existent project");

    }

    @Test
    void testUpdateTaskFields() throws ProjectTrackerPersistingException {
        Task task = new Task();
        task.setName("New Task Name");
        task.setDescription("Updated description");
        task.setPriority(8);

        taskHibernateRepo.save(task);

        task.setName("Updated Task Name");
        Optional<Task> updatedTask = taskHibernateRepo.update(task);

        assertTrue(updatedTask.isPresent());
        assertEquals("Updated Task Name", updatedTask.get().getName());
        assertEquals("Updated description", updatedTask.get().getDescription());
        assertEquals(8, updatedTask.get().getPriority());
    }

    @Test
    void testUpdateNonExistingTask() throws ProjectTrackerPersistingException {
        Task task = new Task();
        task.setId(999L);
        task.setName("Non-existing Task");

        Optional<Task> result = taskHibernateRepo.update(task);
        assertTrue(result.isEmpty());
    }
}