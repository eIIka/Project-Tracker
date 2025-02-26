package ua.ellka.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.model.project.Project;
import ua.ellka.model.task.Task;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;
import ua.ellka.model.user.User;

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
    void testUpdateTaskFields() throws ProjectTrackerPersistingException {
        Task task = taskHibernateRepo.find(1L).get();
        task.setName("Updated Task Name");
        task.setDescription("Updated Description");
        task.setPriority(8);

        Optional<Task> updatedTask = taskHibernateRepo.update(task);

        assertTrue(updatedTask.isPresent());
        assertEquals("Updated Task Name", updatedTask.get().getName());
        assertEquals("Updated Description", updatedTask.get().getDescription());
        assertEquals(8, updatedTask.get().getPriority());
    }

    @Test
    void findTaskByUserTest_manager_success() throws ProjectTrackerPersistingException {
        User manager = new Manager();
        manager.setId(2L);

        List<Task> tasks = taskHibernateRepo.findByUser(manager);

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty(), "Tasks list should not be empty");
        assertTrue(tasks.stream()
                .allMatch(task -> task.getManager().getId().equals(manager.getId())));
    }

    @Test
    void findTaskByUserTest_employee_success() throws ProjectTrackerPersistingException {
        User employee = new Employee();
        employee.setId(4L);

        List<Task> tasks = taskHibernateRepo.findByUser(employee);

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty(), "Tasks list should not be empty");
        assertTrue(tasks.stream()
                .allMatch(task -> task.getEmployee().getId().equals(employee.getId())));
    }

    @Test
    void findTaskByUserTest_userNotFound() throws ProjectTrackerPersistingException {
        User employee = new Employee();
        employee.setId(999L);

        List<Task> tasks = taskHibernateRepo.findByUser(employee);

        assertTrue(tasks.isEmpty(), "Tasks list should not be empty");
    }

    @Test
    void findTaskByProjectTest_success() throws ProjectTrackerPersistingException {
        Project project = new Project();
        project.setId(1L);

        List<Task> tasks = taskHibernateRepo.findByProject(project);

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty(), "Tasks list should not be empty");
        assertTrue(tasks.stream()
                .allMatch(task -> task.getProject().getId().equals(project.getId())));
    }

    @Test
    void findTaskByProjectTest_projectNotFound() throws ProjectTrackerPersistingException {
        Project project = new Project();
        project.setId(999L);

        List<Task> tasks = taskHibernateRepo.findByProject(project);

        assertTrue(tasks.isEmpty(), "Tasks list should not be empty");
    }

    @Test
    void findTaskByProjectAndUserTest_manager_success() throws ProjectTrackerPersistingException {
        Project project = new Project();
        project.setId(1L);

        User manager = new Manager();
        manager.setId(2L);

        List<Task> tasks = taskHibernateRepo.findByProjectAndUser(project, manager);

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty(), "Tasks list should not be empty");
        assertTrue(tasks.stream()
                .allMatch(task -> (
                        task.getProject().getId().equals(project.getId()))
                                  && (task.getManager().getId().equals(manager.getId()))
                )
        );
    }

    @Test
    void findTaskByProjectAndUserTest_employee_success() throws ProjectTrackerPersistingException {
        Project project = new Project();
        project.setId(1L);

        User employee = new Employee();
        employee.setId(4L);

        List<Task> tasks = taskHibernateRepo.findByProjectAndUser(project, employee);

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty(), "Tasks list should not be empty");
        assertTrue(tasks.stream()
                .allMatch(task -> (
                                          task.getProject().getId().equals(project.getId()))
                                  && (task.getEmployee().getId().equals(employee.getId()))
                )
        );
    }

    @Test
    void findTaskByProjectAndUserTest_projectNotFound() throws ProjectTrackerPersistingException {
        Project project = new Project();
        project.setId(999L);

        User employee = new Employee();
        employee.setId(4L);

        List<Task> tasks = taskHibernateRepo.findByProjectAndUser(project, employee);

        assertTrue(tasks.isEmpty(), "Tasks list should not be empty");
    }

    @Test
    void findTaskByProjectAndUserTest_userNotFound() throws ProjectTrackerPersistingException {
        Project project = new Project();
        project.setId(1L);

        User employee = new Employee();
        employee.setId(999L);

        List<Task> tasks = taskHibernateRepo.findByProjectAndUser(project, employee);

        assertTrue(tasks.isEmpty(), "Tasks list should not be empty");
    }
}