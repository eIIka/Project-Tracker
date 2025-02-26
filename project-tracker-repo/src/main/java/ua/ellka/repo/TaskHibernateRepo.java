package ua.ellka.repo;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.model.project.Project;
import ua.ellka.model.task.Task;
import ua.ellka.model.user.User;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TaskHibernateRepo implements TaskRepo {
    private final SessionFactory sessionFactory;

    @Override
    public Optional<Task> find(Long id) throws ProjectTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Task task = session.get(Task.class, id);
            session.getTransaction().commit();
            return Optional.ofNullable(task);
        }
    }

    @Override
    public Optional<Task> findByName(String name) throws ProjectTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            Query<Task> query = session.createQuery("SELECT t FROM Task t WHERE t.name = :name", Task.class);
            query.setParameter("name", name);
            return query.getResultList().stream().findFirst();
        }
    }

    @Override
    public List<Task> findByUser(User user) throws ProjectTrackerPersistingException {
        try(Session session = sessionFactory.openSession()) {
            Query<Task> query = session.createQuery("""
                            SELECT t FROM Task t
                            WHERE t.employee.id = :userId OR t.manager.id = :userId
                            """, Task.class);
            query.setParameter("userId", user.getId());

            return query.getResultList();
        }
    }

    @Override
    public List<Task> findByProject(Project project) throws ProjectTrackerPersistingException {
        try(Session session = sessionFactory.openSession()) {
            Query<Task> query = session.createQuery("""
                            SELECT t FROM Task t
                            WHERE t.project.id = :projectId
                            """, Task.class);
            query.setParameter("projectId", project.getId());

            return query.getResultList();
        }
    }

    @Override
    public List<Task> findByProjectAndUser(Project project, User user) throws ProjectTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            Query<Task> query = session.createQuery("""
                SELECT t FROM Task t
                WHERE t.project.id = :projectId
                AND (t.employee.id = :userId OR t.project.manager.id = :userId)
                """, Task.class);
            query.setParameter("projectId", project.getId());
            query.setParameter("userId", user.getId());

            return query.getResultList();
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
            session.beginTransaction();

            session.remove(task);

            session.getTransaction().commit();

            return Optional.of(task);
        }
    }

    @Override
    public Optional<Task> update(Task task) throws ProjectTrackerPersistingException {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.merge(task);

            session.getTransaction().commit();

            return Optional.of(task);
        }
    }
}
