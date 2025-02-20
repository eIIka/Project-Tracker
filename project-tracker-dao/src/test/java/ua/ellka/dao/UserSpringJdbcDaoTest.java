package ua.ellka.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.ellka.exception.DaoException;
import ua.ellka.mapper.UserRowMapper;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;
import ua.ellka.model.user.User;
import ua.ellka.model.user.UserRole;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserSpringJdbcDaoTest extends DaoTestParent{
    private UserDao userDao;

    @BeforeEach
    void setUp() {
       userDao = new UserSpringJdbcDao(jdbcTemplate, new UserRowMapper());
    }

    @Test
    void getAllUsersTest_success() throws DaoException {
        List<User> allUsers = userDao.findAll();

        assertNotNull(allUsers);
        assertFalse(allUsers.isEmpty());
    }

    @Test
    void findByIdTest_success() throws DaoException {
        Long testId = 1L;
        Optional<User> user = userDao.findById(testId);

        assertTrue(user.isPresent());
        assertEquals(testId, user.get().getId());
    }

    @Test
    void findByIdTest_failure() throws DaoException {
        Long testId = 999L;
        assertThrows(EmptyResultDataAccessException.class, () -> userDao.findById(testId));
    }

    @Test
    void findByNicknameTest_success() throws DaoException {
        String testNickname = "ellka";
        Optional<User> user = userDao.findByNickname(testNickname);

        assertTrue(user.isPresent());
        assertEquals(testNickname, user.get().getNickname());
    }

    @Test
    void findByNicknameTest_failure() throws DaoException {
        String testNickname = "fail";
        assertThrows(EmptyResultDataAccessException.class, () -> userDao.findByNickname(testNickname));
    }

    @Test
    void findByEmailTest_success() throws DaoException {
        String testEmail = "ellka@gmail.com";
        Optional<User> user = userDao.findByEmail(testEmail);

        assertTrue(user.isPresent());
        assertEquals(testEmail, user.get().getEmail());
    }

    @Test
    void findByEmailTest_failure() throws DaoException {
        String testEmail = "fail@gmail.com";
        assertThrows(EmptyResultDataAccessException.class, () -> userDao.findByEmail(testEmail));
    }

    @Test
    void deleteByIdTest_success() throws DaoException {
        Long testId = 1L;
        Optional<User> user = userDao.deleteById(testId);

        assertTrue(user.isPresent());
        assertEquals(testId, user.get().getId());
    }

    @Test
    void deleteByIdTest_failure() throws DaoException {
        Long testId = 999L;
        assertThrows(EmptyResultDataAccessException.class, () -> userDao.deleteById(testId));
    }

    @Test
    void deleteByNicknameTest_success() throws DaoException {
        String testNickname = "ellka";
        Optional<User> user = userDao.deleteByNickname(testNickname);

        assertTrue(user.isPresent());
        assertEquals(testNickname, user.get().getNickname());
    }

    @Test
    void deleteByNicknameTest_failure() throws DaoException {
        String testNickname = "fail";
        assertThrows(EmptyResultDataAccessException.class, () -> userDao.deleteByNickname(testNickname));
    }

    @Test
    void deleteByEmailTest_success() throws DaoException {
        String testEmail = "ellka@gmail.com";
        Optional<User> user = userDao.deleteByEmail(testEmail);

        assertTrue(user.isPresent());
        assertEquals(testEmail, user.get().getEmail());
    }

    @Test
    void deleteByEmailTest_failure() throws DaoException {
        String testEmail = "fail@gmail.com";
        assertThrows(EmptyResultDataAccessException.class, () -> userDao.deleteByEmail(testEmail));
    }

    @Test
    void saveTest_success() throws DaoException {
        User testUser = new Employee();
        testUser.setFirstName("test first name");
        testUser.setLastName("test last name");
        testUser.setEmail("test@gmail.com");
        testUser.setPassword("test password");
        testUser.setNickname("test nickname");
        testUser.setRole(UserRole.EMPLOYEE);
        testUser.setPhoneNumber("1234567843");

        Optional<User> user = userDao.save(testUser);

        assertTrue(user.isPresent());
        assertEquals(testUser.getFirstName(), user.get().getFirstName());
        assertEquals(testUser.getLastName(), user.get().getLastName());
        assertEquals(testUser.getEmail(), user.get().getEmail());
        assertEquals(testUser.getPassword(), user.get().getPassword());
        assertEquals(testUser.getNickname(), user.get().getNickname());
        assertEquals(testUser.getRole(), user.get().getRole());
        assertEquals(testUser.getPhoneNumber(), user.get().getPhoneNumber());
    }

    @Test
    void saveTest_ifEmailAlreadyExistsThrowsException() throws DaoException {
        User testUser = new Employee();
        testUser.setFirstName("test first name");
        testUser.setLastName("test last name");
        testUser.setEmail("ellka@gmail.com");
        testUser.setPassword("test password");
        testUser.setNickname("test nickname");
        testUser.setRole(UserRole.EMPLOYEE);
        testUser.setPhoneNumber("1234567843");

        assertThrows(DuplicateKeyException.class, () -> userDao.save(testUser));
    }

    @Test
    void saveTest_ifNicknameAlreadyExistsThrowsException() throws DaoException {
        User testUser = new Employee();
        testUser.setFirstName("test first name");
        testUser.setLastName("test last name");
        testUser.setEmail("test@gmail.com");
        testUser.setPassword("test password");
        testUser.setNickname("ellka");
        testUser.setRole(UserRole.EMPLOYEE);
        testUser.setPhoneNumber("1234567843");

        assertThrows(DuplicateKeyException.class, () -> userDao.save(testUser));
    }

    @Test
    void saveTest_ifPhoneNumberAlreadyExistsThrowsException() throws DaoException {
        User testUser = new Employee();
        testUser.setFirstName("test first name");
        testUser.setLastName("test last name");
        testUser.setEmail("test@gmail.com");
        testUser.setPassword("test password");
        testUser.setNickname("test nickname");
        testUser.setRole(UserRole.EMPLOYEE);
        testUser.setPhoneNumber("0688228575");

        assertThrows(DuplicateKeyException.class, () -> userDao.save(testUser));
    }

    @Test
    void updateTest_success() throws DaoException {
        User testUser = new Manager();
        testUser.setFirstName("test first name");
        testUser.setLastName("test last name");
        testUser.setEmail("test@gmail.com");
        testUser.setPassword("test password");
        testUser.setNickname("test nickname");
        testUser.setRole(UserRole.MANAGER);
        testUser.setPhoneNumber("0688228512");

        Long testUpdateById = 1L;

        Optional<User> user = userDao.updateById(testUpdateById, testUser);

        assertTrue(user.isPresent());
        assertEquals(testUser.getFirstName(), user.get().getFirstName());
        assertEquals(testUser.getLastName(), user.get().getLastName());
        assertEquals(testUser.getEmail(), user.get().getEmail());
        assertEquals(testUser.getPassword(), user.get().getPassword());
        assertEquals(testUser.getNickname(), user.get().getNickname());
        assertEquals(testUser.getRole(), user.get().getRole());
        assertEquals(testUser.getPhoneNumber(), user.get().getPhoneNumber());
    }

    @Test
    void updateTest_ifEmailAlreadyExistsThrowsException() throws DaoException {
        User testUser = new Manager();
        testUser.setFirstName("test first name");
        testUser.setLastName("test last name");
        testUser.setEmail("ellka@gmail.com");
        testUser.setPassword("test password");
        testUser.setNickname("test nickname");
        testUser.setRole(UserRole.MANAGER);
        testUser.setPhoneNumber("0688228512");

        Long testUpdateById = 1L;

        assertThrows(DuplicateKeyException.class, () -> userDao.updateById(testUpdateById, testUser));
    }

    @Test
    void updateTest_ifNicknameAlreadyExistsThrowsException() throws DaoException {
        User testUser = new Manager();
        testUser.setFirstName("test first name");
        testUser.setLastName("test last name");
        testUser.setEmail("test@gmail.com");
        testUser.setPassword("test password");
        testUser.setNickname("ellka");
        testUser.setRole(UserRole.MANAGER);
        testUser.setPhoneNumber("0688228512");

        Long testUpdateById = 1L;

        assertThrows(DuplicateKeyException.class, () -> userDao.updateById(testUpdateById, testUser));
    }

    @Test
    void updateTest_ifPhoneNumberAlreadyExistsThrowsException() throws DaoException {
        User testUser = new Manager();
        testUser.setFirstName("test first name");
        testUser.setLastName("test last name");
        testUser.setEmail("test@gmail.com");
        testUser.setPassword("test password");
        testUser.setNickname("test nickname");
        testUser.setRole(UserRole.MANAGER);
        testUser.setPhoneNumber("0688228575");

        Long testUpdateById = 1L;

        assertThrows(DuplicateKeyException.class, () -> userDao.updateById(testUpdateById, testUser));
    }

    @Test
    void tearDown() {
        System.out.println("Test finished");
    }
}