package ua.ellka.repo;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.model.project.Project;
import ua.ellka.model.user.User;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ProjectHibernateRepo implements ProjectRepo {
    private final SessionFactory sessionFactory;

    @Override
    public Optional<Project> find(Long id) throws ProjectTrackerPersistingException {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Project project = session.get(Project.class, id);
            session.getTransaction().commit();
            return Optional.ofNullable(project);
        }
    }

    @Override
    public Optional<Project> findByName(String name) throws ProjectTrackerPersistingException {
        try(Session session = sessionFactory.openSession()) {
            Query<Project> query = session.createQuery("SELECT p FROM Project p where p.name = :name", Project.class);
            query.setParameter("name", name);

            return query.uniqueResultOptional();
        }
    }

    @Override
    public List<Project> findByUser(User user) throws ProjectTrackerPersistingException {
        try(Session session = sessionFactory.openSession()) {
            Query<Project> query = session.createQuery("""
                    SELECT DISTINCT p FROM Project p
                    LEFT JOIN FETCH p.employees e
                    LEFT JOIN p.manager m
                    WHERE m.id = :userId OR e.id = :userId
                    """, Project.class);
            query.setParameter("userId", user.getId());

            return query.getResultList();
        }
    }

    @Override
    public Optional<Project> save(Project project) throws ProjectTrackerPersistingException {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.persist(project);
            session.flush();

            session.getTransaction().commit();

            return Optional.ofNullable(project);
        }
    }

    @Override
    public Optional<Project> delete(Project project) throws ProjectTrackerPersistingException {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.remove(project);

            session.getTransaction().commit();

            return Optional.of(project);
        }
    }

    @Override
    public Optional<Project> update(Project project) throws ProjectTrackerPersistingException {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.merge(project);

            session.getTransaction().commit();

            return Optional.of(project);
        }
    }
}
