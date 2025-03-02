package ua.ellka.repo;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.model.user.User;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserHibernateRepo implements UserRepo {
    private final SessionFactory sessionFactory;

    @Override
    public Optional<User> find(Long id) throws ProjectTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.getTransaction().commit();
            return Optional.ofNullable(user);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) throws ProjectTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return Optional.ofNullable((User) query.uniqueResult());
        }
    }

    @Override
    public Optional<User> findByNickname(String nickname) throws ProjectTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("SELECT u FROM User u WHERE u.nickname = :nickname", User.class);
            query.setParameter("nickname", nickname);
            return Optional.ofNullable((User) query.uniqueResult());
        }
    }

    @Override
    public Optional<User> delete(User user) throws ProjectTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.remove(user);

            session.getTransaction().commit();

            return Optional.of(user);
        }
    }

    @Override
    public Optional<User> save(User user) throws ProjectTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.persist(user);
            session.flush();

            session.getTransaction().commit();

            return Optional.ofNullable(user);
        }
    }

    @Override
    public Optional<User> update(User user) throws ProjectTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.merge(user);
            //session.flush();

            session.getTransaction().commit();

            return Optional.of(user);
        }
    }
}
