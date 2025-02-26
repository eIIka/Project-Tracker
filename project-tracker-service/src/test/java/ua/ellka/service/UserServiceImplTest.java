package ua.ellka.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import ua.ellka.dto.EmployeeDTO;
import ua.ellka.dto.ManagerDTO;
import ua.ellka.dto.UserDTO;
import ua.ellka.exception.NotFoundServiceException;
import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.exception.ServiceException;
import ua.ellka.mapper.UserMapper;
import ua.ellka.model.project.Project;
import ua.ellka.model.task.Task;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;
import ua.ellka.model.user.User;
import ua.ellka.repo.UserRepo;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    private UserService userService;
    private UserRepo userRepo;

    @BeforeEach
    void setUp() {
        userRepo = mock(UserRepo.class);
        UserMapper userMapper = UserMapper.INSTANCE;

        userService = new UserServiceImpl(userRepo, userMapper);
    }

    @Test
    void createUserTest_createEmployee() throws ProjectTrackerPersistingException {
        UserDTO userDTO = new EmployeeDTO();
        userDTO.setNickname("Test");
        userDTO.setEmail("test@test.com");
        userDTO.setRole("Employee");
        userDTO.setFirstName("Vlad");
        userDTO.setLastName("Kalkatin");

        User mockUser = new Employee();
        mockUser.setEmail("test@test.com");
        mockUser.setFirstName("Vlad");
        mockUser.setLastName("Kalkatin");
        mockUser.setNickname("Test");

        when(userRepo.findByNickname(anyString())).thenReturn(Optional.empty());
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepo.save(any())).thenReturn(Optional.of(mockUser));

        UserDTO created = userService.createUser(userDTO);

        assertNotNull(created);
        assertEquals(userDTO.getNickname(), created.getNickname());
        assertEquals(userDTO.getEmail(), created.getEmail());
        assertEquals(userDTO.getFirstName(), created.getFirstName());
        assertEquals(userDTO.getLastName(), created.getLastName());
        assertEquals(userDTO.getRole(), created.getRole());
    }

    @Test
    void createUserTest_createManager() throws ProjectTrackerPersistingException {
        UserDTO userDTO = new ManagerDTO();
        userDTO.setNickname("Test");
        userDTO.setEmail("test@test.com");
        userDTO.setRole("Employee");
        userDTO.setFirstName("Vlad");
        userDTO.setLastName("Kalkatin");

        User mockUser = new Manager();
        mockUser.setEmail("test@test.com");
        mockUser.setFirstName("Vlad");
        mockUser.setLastName("Kalkatin");
        mockUser.setNickname("Test");

        when(userRepo.findByNickname(anyString())).thenReturn(Optional.empty());
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepo.save(any())).thenReturn(Optional.of(mockUser));

        UserDTO created = userService.createUser(userDTO);

        assertNotNull(created);
        assertEquals(userDTO.getNickname(), created.getNickname());
        assertEquals(userDTO.getEmail(), created.getEmail());
        assertEquals(userDTO.getFirstName(), created.getFirstName());
        assertEquals(userDTO.getLastName(), created.getLastName());
        assertEquals(userDTO.getRole(), created.getRole());
    }

    @Test
    void createUserTest_existingUserWithNicknameThrowException() throws ProjectTrackerPersistingException {
        UserDTO userDTO = new EmployeeDTO();
        userDTO.setNickname("Test");
        userDTO.setEmail("test@test.com");
        userDTO.setFirstName("Vlad");
        userDTO.setLastName("Kalkatin");

        User mockUser = new Employee();
        mockUser.setFirstName("Vlad");
        mockUser.setLastName("Kalkatin");
        mockUser.setNickname("Test");

        when(userRepo.findByNickname(anyString())).thenReturn(Optional.of(mockUser));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> userService.createUser(userDTO));
        assertEquals(exception.getMessage(), "User with nickname " + userDTO.getNickname() + " already exists");
    }

    @Test
    void createUserTest_existingUserWithEmailThrowException() throws ProjectTrackerPersistingException {
        UserDTO userDTO = new EmployeeDTO();
        userDTO.setNickname("Test");
        userDTO.setEmail("test@test.com");
        userDTO.setFirstName("Vlad");
        userDTO.setLastName("Kalkatin");

        User mockUser = new Employee();
        mockUser.setFirstName("Vlad");
        mockUser.setLastName("Kalkatin");
        mockUser.setNickname("Test");
        mockUser.setEmail("test@test.com");

        when(userRepo.findByNickname(anyString())).thenReturn(Optional.empty());
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(mockUser));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> userService.createUser(userDTO));
        assertEquals(exception.getMessage(), "User with email " + userDTO.getEmail() + " already exists");
    }

    @Test
    void updateUserTest_updateEmployee() throws ProjectTrackerPersistingException {
        User mockUser = new Employee();
        mockUser.setFirstName("First Name");
        mockUser.setLastName("Last Name");
        mockUser.setNickname("Employee");
        mockUser.setEmail("test@test.com");

        UserDTO testUserDTO = new EmployeeDTO();
        testUserDTO.setNickname("New Test Employee");
        testUserDTO.setEmail("newtest@newtest.com");
        testUserDTO.setFirstName("New First Name");
        testUserDTO.setLastName("New Last Name");
        testUserDTO.setPhoneNumber("1234567890");

        when(userRepo.find(anyLong())).thenReturn(Optional.of(mockUser));
        when(userRepo.findByNickname(anyString())).thenReturn(Optional.empty());
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepo.update(any())).thenReturn(Optional.of(mockUser));

        UserDTO updated = userService.updateUser(1L, testUserDTO);

        assertNotNull(updated);
        assertEquals(testUserDTO.getNickname(), updated.getNickname());
        assertEquals(testUserDTO.getEmail(), updated.getEmail());
        assertEquals(testUserDTO.getFirstName(), updated.getFirstName());
        assertEquals(testUserDTO.getLastName(), updated.getLastName());
        assertEquals(testUserDTO.getPhoneNumber(), updated.getPhoneNumber());
        assertEquals(testUserDTO.getRole(), updated.getRole());
    }

    @Test
    void updateUserTest_updateManager() throws ProjectTrackerPersistingException {
        User mockUser = new Manager();
        mockUser.setFirstName("First Name");
        mockUser.setLastName("Last Name");
        mockUser.setNickname("Manager");
        mockUser.setEmail("test@test.com");

        UserDTO testUserDTO = new ManagerDTO();
        testUserDTO.setNickname("New Test Manager");
        testUserDTO.setEmail("newtest@newtest.com");
        testUserDTO.setFirstName("New First Name");
        testUserDTO.setLastName("New Last Name");
        testUserDTO.setPhoneNumber("1234567890");

        when(userRepo.find(anyLong())).thenReturn(Optional.of(mockUser));
        when(userRepo.findByNickname(anyString())).thenReturn(Optional.empty());
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepo.update(any())).thenReturn(Optional.of(mockUser));

        UserDTO updated = userService.updateUser(1L, testUserDTO);

        assertNotNull(updated);
        assertEquals(testUserDTO.getNickname(), updated.getNickname());
        assertEquals(testUserDTO.getEmail(), updated.getEmail());
        assertEquals(testUserDTO.getFirstName(), updated.getFirstName());
        assertEquals(testUserDTO.getLastName(), updated.getLastName());
        assertEquals(testUserDTO.getPhoneNumber(), updated.getPhoneNumber());
        assertEquals(testUserDTO.getRole(), updated.getRole());
    }

    @Test
    void updateUserTest_nonExistingUserThrowException() throws ProjectTrackerPersistingException {
        when(userRepo.find(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> userService.updateUser(1L, new ManagerDTO()));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void updateUserTest_existingUserWithNicknameThrowException() throws ProjectTrackerPersistingException {
        UserDTO testUserDTO = new ManagerDTO();
        testUserDTO.setNickname("New Test Manager");
        testUserDTO.setEmail("newtest@newtest.com");
        testUserDTO.setFirstName("New First Name");

        User existingUser = new Manager();
        existingUser.setId(1L);
        existingUser.setFirstName("First Name");
        existingUser.setNickname("Test Nickname");

        User anotherUser = new Manager();
        anotherUser.setId(2L);
        anotherUser.setFirstName("First Name");
        anotherUser.setNickname("New Test Manager");

        when(userRepo.find(anyLong())).thenReturn(Optional.of(existingUser));
        when(userRepo.findByNickname(testUserDTO.getNickname())).thenReturn(Optional.of(anotherUser));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> userService.updateUser(1L, testUserDTO));
        assertEquals("User with nickname " + testUserDTO.getNickname() + " already exists", exception.getMessage());
    }

    @Test
    void updateUserTest_existingUserWithEmailThrowException() throws ProjectTrackerPersistingException {
        UserDTO testUserDTO = new ManagerDTO();
        testUserDTO.setNickname("New Test Manager");
        testUserDTO.setEmail("newtest@newtest.com");
        testUserDTO.setFirstName("New First Name");

        User existingUser = new Manager();
        existingUser.setId(1L);
        existingUser.setEmail("test@test.com");
        existingUser.setFirstName("First Name");
        existingUser.setNickname("Test Nickname");

        User anotherUser = new Manager();
        anotherUser.setId(2L);
        anotherUser.setEmail("newtest@newtest.com");
        anotherUser.setFirstName("First Name");
        anotherUser.setNickname("New Test Manager");

        when(userRepo.find(anyLong())).thenReturn(Optional.of(existingUser));
        when(userRepo.findByNickname(testUserDTO.getNickname())).thenReturn(Optional.empty());
        when(userRepo.findByEmail(testUserDTO.getEmail())).thenReturn(Optional.of(anotherUser));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> userService.updateUser(1L, testUserDTO));
        assertEquals("User with email " + testUserDTO.getEmail() + " already exists", exception.getMessage());
    }

    @Test
    void updateUserTest_fails() throws ProjectTrackerPersistingException {
        UserDTO employeeDTO = new EmployeeDTO();
        when(userRepo.find(anyLong())).thenReturn(Optional.of(new Manager()));
        when(userRepo.findByNickname(any())).thenReturn(Optional.empty());
        when(userRepo.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepo.update(any())).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class,
                () -> userService.updateUser(1L, new EmployeeDTO()));
        assertEquals("User could not be updated", exception.getMessage());
    }

    @Test
    void getUserTest_getEmployee() throws ProjectTrackerPersistingException {
        User employee = new Employee();
        employee.setId(1L);
        employee.setNickname("New Test Manager");
        employee.setEmail("newtest@newtest.com");
        employee.setFirstName("New First Name");
        employee.setLastName("New Last Name");
        employee.setPhoneNumber("1234567890");

        when(userRepo.find(anyLong())).thenReturn(Optional.of(employee));

        UserDTO user = userService.getUser(1L);

        assertEquals(employee.getNickname(), user.getNickname());
        assertEquals(employee.getEmail(), user.getEmail());
        assertEquals(employee.getFirstName(), user.getFirstName());
        assertEquals(employee.getLastName(), user.getLastName());
        assertEquals(employee.getPhoneNumber(), user.getPhoneNumber());
        assertEquals(employee.getRole().getRole(), user.getRole());
    }

    @Test
    void getUserTest_getManager() throws ProjectTrackerPersistingException {
        User manager = new Manager();
        manager.setId(1L);
        manager.setNickname("New Test Manager");
        manager.setEmail("newtest@newtest.com");
        manager.setFirstName("New First Name");
        manager.setLastName("New Last Name");
        manager.setPhoneNumber("1234567890");

        when(userRepo.find(anyLong())).thenReturn(Optional.of(manager));

        UserDTO user = userService.getUser(1L);

        assertEquals(manager.getNickname(), user.getNickname());
        assertEquals(manager.getEmail(), user.getEmail());
        assertEquals(manager.getFirstName(), user.getFirstName());
        assertEquals(manager.getLastName(), user.getLastName());
        assertEquals(manager.getPhoneNumber(), user.getPhoneNumber());
        assertEquals(manager.getRole().getRole(), user.getRole());
    }

    @Test
    void getUserTest_nonExistingUserThrowException() throws ProjectTrackerPersistingException {
        when(userRepo.find(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> userService.getUser(1L));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void deleteUserTest_deletedEmployee() throws ProjectTrackerPersistingException {
        User employee = new Employee();
        employee.setId(1L);
        employee.setNickname("New Test Manager");
        employee.setEmail("newtest@newtest.com");
        employee.setFirstName("New First Name");
        employee.setLastName("New Last Name");
        employee.setPhoneNumber("1234567890");

        when(userRepo.find(anyLong())).thenReturn(Optional.of(employee));
        when(userRepo.delete(any())).thenReturn(Optional.of(employee));

        UserDTO user = userService.deleteUser(1L);

        assertEquals(employee.getId(), user.getId());
        assertEquals(employee.getNickname(), user.getNickname());
        assertEquals(employee.getEmail(), user.getEmail());
        assertEquals(employee.getFirstName(), user.getFirstName());
        assertEquals(employee.getLastName(), user.getLastName());
        assertEquals(employee.getPhoneNumber(), user.getPhoneNumber());
    }

    @Test
    void deleteUserTest_deletedManager() throws ProjectTrackerPersistingException {
        User manager = new Manager();
        manager.setId(1L);
        manager.setNickname("New Test Manager");
        manager.setEmail("newtest@newtest.com");
        manager.setFirstName("New First Name");
        manager.setLastName("New Last Name");
        manager.setPhoneNumber("1234567890");

        when(userRepo.find(anyLong())).thenReturn(Optional.of(manager));
        when(userRepo.delete(any())).thenReturn(Optional.of(manager));

        UserDTO user = userService.deleteUser(1L);

        assertEquals(manager.getId(), user.getId());
        assertEquals(manager.getNickname(), user.getNickname());
        assertEquals(manager.getEmail(), user.getEmail());
        assertEquals(manager.getFirstName(), user.getFirstName());
        assertEquals(manager.getLastName(), user.getLastName());
        assertEquals(manager.getPhoneNumber(), user.getPhoneNumber());
    }

    @Test
    void deleteUserTest_nonExistingUserThrowException() throws ProjectTrackerPersistingException {
        when(userRepo.find(anyLong())).thenReturn(Optional.empty());

        NotFoundServiceException exception = assertThrows(NotFoundServiceException.class,
                () -> userService.deleteUser(1L));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void deleteUserTest_fails() throws ProjectTrackerPersistingException {
        User employee = new Employee();
        employee.setId(1L);
        employee.setNickname("New Test Manager");
        employee.setEmail("newtest@newtest.com");
        employee.setFirstName("New First Name");
        employee.setLastName("New Last Name");
        employee.setPhoneNumber("1234567890");

        when(userRepo.find(anyLong())).thenReturn(Optional.of(employee));
        when(userRepo.delete(any())).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class,
                () -> userService.deleteUser(1L));
        assertEquals("User could not be deleted", exception.getMessage());
    }
}