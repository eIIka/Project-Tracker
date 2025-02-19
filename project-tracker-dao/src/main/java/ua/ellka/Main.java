package ua.ellka;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.ellka.config.JdbcConfig;
import ua.ellka.dao.UserDao;
import ua.ellka.dao.UserMySqlDao;
import ua.ellka.dao.UserSpringJdbcDao;
import ua.ellka.exception.DaoException;
import ua.ellka.mapper.UserRowMapper;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;
import ua.ellka.model.user.User;
import ua.ellka.model.user.UserRole;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {

        String username = System.getenv("JDBC_DB_USER");
        String password = System.getenv("JDBC_DB_PASSWORD");
        String dbname = System.getenv("JDBC_DB_NAME");
        //String url = "jdbc:mysql://localhost:3306/" + dbname;
        String url = "jdbc:mysql://127.0.0.1:3306/" + dbname;

        Class.forName("com.mysql.cj.jdbc.Driver"); // load driver class

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            UserMySqlDao dao = new UserMySqlDao(connection);
//
//            List<User> users = dao.findAll();
            Optional<User> byId = dao.findById(1L);
//            Optional<User> byNickname = dao.findByNickname("alice_j");
//            Optional<User> byEmail = dao.findByEmail("evan.foster@example.com");
//
            User user = new Employee();
            user.setNickname("ellka");
            user.setFirstName("Vlad");
            user.setLastName("Kalkatin");
            user.setEmail("vkalkatins@gmail.com");
            user.setPassword("password");
            user.setPhoneNumber("0688228575");
//            Optional<User> savedUser = dao.save(user);
//            User userSaved = savedUser.get();
//
//            Optional<User> deletedById = dao.deleteById(1L);
//            Optional<User> deleteByEmail = dao.deleteByEmail("vkalkatins@gmail.com");
//            Optional<User> deleteByNickname = dao.deleteByNickname("george_h");


            Optional<User> updateUser = dao.updateById(3L,user);

        } catch (SQLException | DaoException e) {
            System.out.println("Unable to connect to database");
        }


        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JdbcConfig.class)) {
            JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
            UserRowMapper userRowMapper = context.getBean(UserRowMapper.class);
            UserDao userDao = new UserSpringJdbcDao(jdbcTemplate, userRowMapper);

            User newUser = new Employee();
            newUser.setNickname("new nickname");
            newUser.setFirstName("new firstname");
            newUser.setLastName("new lastname");
            newUser.setEmail("newemail@gmail.com");
            newUser.setPassword("password");
            newUser.setPhoneNumber("1234567685");
            newUser.setRole(UserRole.EMPLOYEE);

//            Optional<User> savedUser = userDao.save(newUser);
//            System.out.println("Saved User: " + savedUser.orElse(null));

            Optional<User> updateUser = userDao.updateById(1L, newUser);
            System.out.println("Updated User: " + updateUser.orElse(null));
        } catch (DaoException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}