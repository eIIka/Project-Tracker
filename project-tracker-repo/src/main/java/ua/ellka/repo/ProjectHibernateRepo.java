package ua.ellka.repo;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.model.project.Project;

import java.util.Optional;

@RequiredArgsConstructor
public class ProjectHibernateRepo implements ProjectRepo {
    private final SessionFactory sessionFactory;

    @Override
    public Optional<Project> find(Long id) throws ProjectTrackerPersistingException {
        try(Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Project.class, id));
        }
    }

    @Override
    public Optional<Project> findByName(String name) throws ProjectTrackerPersistingException {
        try(Session session = sessionFactory.openSession()) {
            Query<Project> query = session.createQuery("from Project where name = :name", Project.class);
            query.setParameter("name", name);

            return query.uniqueResultOptional();
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
    public Optional<Project> delete(Long id) throws ProjectTrackerPersistingException {
        try(Session session = sessionFactory.openSession()) {
            Project projectDelete = session.find(Project.class, id);

            if (projectDelete == null) {
                return Optional.empty();
            }
            session.beginTransaction();

            session.remove(projectDelete);

            session.getTransaction().commit();

            return Optional.ofNullable(projectDelete);
        }
    }

    @Override
    public Optional<Project> deleteByProject(Project project) throws ProjectTrackerPersistingException {
        try(Session session = sessionFactory.openSession()) {
            Project projectDelete = session.find(Project.class, project.getId());

            if (projectDelete == null) {
                return Optional.empty();
            }

            session.beginTransaction();
            session.remove(projectDelete);

            session.getTransaction().commit();
            return Optional.ofNullable(projectDelete);
        }
    }

    @Override
    public Optional<Project> update(Project project) throws ProjectTrackerPersistingException {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Project existingProject = session.find(Project.class, project.getId());
            if (existingProject == null) {
                return Optional.empty();
            }

            if (project.getName() != null) {
                existingProject.setName(project.getName());
            }

            if (project.getDescription() != null) {
                existingProject.setDescription(project.getDescription());
            }

            if (project.getStatus() != null) {
                existingProject.setStatus(project.getStatus());
            }

            if (project.getPriority() > 0 && project.getPriority() < 11) {
                existingProject.setPriority(project.getPriority());
            }

            if (project.getCreatedAt() != null) {
                existingProject.setCreatedAt(project.getCreatedAt());
            }

            if (project.getUpdatedAt() != null) {
                existingProject.setUpdatedAt(project.getUpdatedAt());
            }

            if (project.getManager() != null) {
                existingProject.setManager(project.getManager());
            }

            if (project.getDeadline() != null) {
                existingProject.setDeadline(project.getDeadline());
            }

            if (project.getEmployees() != null) {
                existingProject.setEmployees(project.getEmployees());
            }

            if (project.getTasks() != null) {
                existingProject.setTasks(project.getTasks());
            }

            session.merge(existingProject);

            session.getTransaction().commit();
            return Optional.ofNullable(existingProject);
        }
    }
}
