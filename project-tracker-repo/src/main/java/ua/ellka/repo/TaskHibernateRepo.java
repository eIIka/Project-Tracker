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
}
