package ua.ellka.repo;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.model.task.Task;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TaskHibernateRepo implements TaskRepo {
    private final SessionFactory sessionFactory;

    @Override
    public Optional<Task> find(Long id) throws ProjectTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Task.class, id));
        }
    }

    @Override
    public Optional<Task> findByName(String name) throws ProjectTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            Query<Task> query = session.createQuery("from Task where name = :name", Task.class);
            query.setParameter("name", name);
            return query.uniqueResultOptional();
        }
    }

    @Override
    public Optional<Task> save(Task task) throws ProjectTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(task);
            session.flush();

            session.getTransaction().commit();
            return Optional.ofNullable(task);

        }
    }

    @Override
    public Optional<Task> delete(Task task) throws ProjectTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            Task taskToDelete = session.find(Task.class, task.getId());

            if (taskToDelete == null) {
                return Optional.empty();
            }

            session.beginTransaction();
            session.remove(taskToDelete);

            session.getTransaction().commit();
            return Optional.ofNullable(taskToDelete);
        }
    }

    @Override
    public List<Task> findAllByProjectId(Long projectId) throws ProjectTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            Query<Task> query = session.createQuery("from Task where project.id = :projectId", Task.class);
            query.setParameter("projectId", projectId);
            return query.getResultList();
        }
    }

    @Override
    public Optional<Task> update(Task task) throws ProjectTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Task existingTask = session.find(Task.class, task.getId());
            if (existingTask == null) {
                return Optional.empty();
            }

            if (task.getName() != null) {
                existingTask.setName(task.getName());
            }

            if (task.getDescription() != null) {
                existingTask.setDescription(task.getDescription());
            }

            if (task.getPendingStatus() != null) {
                existingTask.setPendingStatus(task.getPendingStatus());
            }

            if (task.getExecutionDetails() != null) {
                existingTask.setExecutionDetails(task.getExecutionDetails());
            }

            if (task.getStatus() != null) {
                existingTask.setStatus(task.getStatus());
            }

            if (task.getType() != null) {
                existingTask.setType(task.getType());
            }

            if (task.getPriority() != 0) {
                existingTask.setPriority(task.getPriority());
            }

            if (task.getCreatedAt() != null) {
                existingTask.setCreatedAt(task.getCreatedAt());
            }

            if (task.getUpdatedAt() != null) {
                existingTask.setUpdatedAt(task.getUpdatedAt());
            }

            if (task.getEndDate() != null) {
                existingTask.setEndDate(task.getEndDate());
            }

            if (task.getDeadline() != null) {
                existingTask.setDeadline(task.getDeadline());
            }

            if (task.getHistory() != null) {
                existingTask.setHistory(task.getHistory());
            }

            if (task.getComments() != null) {
                existingTask.setComments(task.getComments());
            }

            if (task.getEmployee() != null) {
                existingTask.setEmployee(task.getEmployee());
            }

            if (task.getManager() != null) {
                existingTask.setManager(task.getManager());
            }

            if (task.getProject() != null) {
                existingTask.setProject(task.getProject());
            }

            session.merge(existingTask);

            session.getTransaction().commit();
            return Optional.ofNullable(existingTask);
        }
    }
}
