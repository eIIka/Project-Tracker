package ua.ellka.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.model.project.Project;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.User;
import ua.ellka.model.user.UserRole;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserHibernateRepoTest extends RepoParent{
    private UserHibernateRepo userHibernateRepo;

    @BeforeEach
    void setUp() {
        userHibernateRepo = new UserHibernateRepo(sessionFactory);
    }

    @Test
    void findTest_success() throws ProjectTrackerPersistingException {
        Long userId = 1L;
        Optional<User> user = userHibernateRepo.find(userId);

        assertTrue(user.isPresent(), "user not found");
        assertNotNull(user.get().getId());
        assertEquals(userId, user.get().getId());
    }

    @Test
    void findTest_notFound() throws ProjectTrackerPersistingException {
        Long userId = 999L;
        Optional<User> user = userHibernateRepo.find(userId);

        assertTrue(user.isEmpty(), "user not found");
    }

    @Test
    void findByEmailTest_success() throws ProjectTrackerPersistingException {
        String email = "manager1@example.com";
        Optional<User> user = userHibernateRepo.findByEmail(email);

        assertTrue(user.isPresent(), "user not found");
        assertNotNull(user.get());
        assertNotNull(user.get().getId());
        assertEquals(email, user.get().getEmail());
    }

    @Test
    void findByEmailTest_notFound() throws ProjectTrackerPersistingException {
        String email = "test@example.com";
        Optional<User> user = userHibernateRepo.findByEmail(email);

        assertTrue(user.isEmpty(), "user not found");
    }

    @Test
    void findByNicknameTest_success() throws ProjectTrackerPersistingException {
        String nickname = "manager1";
        Optional<User> user = userHibernateRepo.findByNickname(nickname);

        assertTrue(user.isPresent(), "user not found");
        assertNotNull(user.get());
        assertNotNull(user.get().getId());
        assertEquals(nickname, user.get().getNickname());
    }

    @Test
    void findByNicknameTest_notFound() throws ProjectTrackerPersistingException {
        String nickname = "test";
        Optional<User> user = userHibernateRepo.findByNickname(nickname);

        assertTrue(user.isEmpty(), "user not found");
    }

    @Test
    void saveTest_idNotNul() throws ProjectTrackerPersistingException {
        User user = new Employee();
        user.setNickname("test nickname");
        user.setFirstName("test first name");
        user.setLastName("test last name");
        user.setEmail("test@gmail.com");
        user.setPassword("employeetestpsw");
        user.setRegisteredAt(LocalDateTime.now());

        Optional<User> saved = userHibernateRepo.save(user);
        assertTrue(saved.isPresent(), "user not saved");

        User savedUser = saved.get();

        assertNotNull(savedUser.getId());
        assertEquals("test nickname", savedUser.getNickname());
        assertEquals("test first name", savedUser.getFirstName());
        assertEquals("test last name", savedUser.getLastName());
        assertEquals("test@gmail.com", savedUser.getEmail());
        assertEquals("employeetestpsw", savedUser.getPassword());
        assertEquals(UserRole.EMPLOYEE, savedUser.getRole());
        assertNotNull(savedUser.getRegisteredAt());
    }

    @Test
    void deleteTest_success() throws ProjectTrackerPersistingException {
        Long userId = 1L;

        Optional<User> user = userHibernateRepo.find(userId);
        assertTrue(user.isPresent(), "user not found");

        Optional<User> deleted = userHibernateRepo.delete(userId);
        assertTrue(deleted.isPresent(), "user not found");
        assertEquals(userId, deleted.get().getId());

        Optional<User> deletedUser = userHibernateRepo.find(userId);
        assertTrue(deletedUser.isEmpty(), "user not found");
    }

    @Test
    void deleteTest_notFound() throws ProjectTrackerPersistingException {
        Long userId = 999L;
        Optional<User> deleted = userHibernateRepo.delete(userId);

        assertTrue(deleted.isEmpty(), "user not found");
    }

    @Test
    void deleteByUserTest_success() throws ProjectTrackerPersistingException {
        Optional<User> userOptional = userHibernateRepo.findByNickname("employee0");
        assertTrue(userOptional.isPresent(), "Project Alpha not found");

        User user = userOptional.get();
        Optional<User> deleted = userHibernateRepo.deleteByUser(user);

        assertTrue(deleted.isPresent(), "Deleted project not found");
        assertEquals(user.getId(), deleted.get().getId());

        Optional<User> fromDb = userHibernateRepo.findByNickname("employee0");
        assertTrue(fromDb.isEmpty(), "Project Alpha was not deleted");
    }

    @Test
    void deleteByUserTest_notFound() throws ProjectTrackerPersistingException {
        User user = new Employee();
        user.setId(999L);

        Optional<User> deleted = userHibernateRepo.deleteByUser(user);

        assertTrue(deleted.isEmpty(), "user not found");
    }
}