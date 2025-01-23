package ua.ellka.repo;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.model.user.User;

import java.util.Optional;

@RequiredArgsConstructor
public class UserHibernateRepo implements UserRepo {
    private final SessionFactory sessionFactory;

    @Override
    public Optional<User> find(Long id) throws ProjectTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(User.class, id));
        }
    }

    @Override
    public Optional<User> findByEmail(String email) throws ProjectTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from User where email = :email");
            query.setParameter("email", email);
            return Optional.ofNullable((User) query.uniqueResult());
        }
    }

    @Override
    public Optional<User> findByNickname(String nickname) throws ProjectTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from User where nickname = :nickname");
            query.setParameter("nickname", nickname);
            return Optional.ofNullable((User) query.uniqueResult());
        }
    }

    @Override
    public Optional<User> delete(Long id) throws ProjectTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            User userDelete = session.find(User.class, id);

            if (userDelete == null) {
                return Optional.empty();
            }

            session.beginTransaction();

            session.remove(userDelete);

            session.getTransaction().commit();

            return Optional.ofNullable(userDelete);
        }
    }

    @Override
    public Optional<User> deleteByUser(User user) throws ProjectTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            User userDelete = session.find(User.class, user.getId());

            if (userDelete == null) {
                return Optional.empty();
            }

            session.beginTransaction();
            session.remove(userDelete);

            session.getTransaction().commit();
            return Optional.ofNullable(userDelete);
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
}
