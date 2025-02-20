package ua.ellka.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;
import ua.ellka.model.user.User;
import ua.ellka.model.user.UserRole;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user;
        UserRole userRole = UserRole.valueOf(resultSet.getString("users.user_role").toUpperCase());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        if (userRole == UserRole.EMPLOYEE){
            user = new Employee();
        } else {
            user = new Manager();
        }

        user.setId(resultSet.getLong("users.id"));
        user.setNickname(resultSet.getString("users.nickname"));
        user.setFirstName(resultSet.getString("users.first_name"));
        user.setLastName(resultSet.getString("users.last_name"));
        user.setPassword(resultSet.getString("users.password"));
        user.setPhoneNumber(resultSet.getString("users.phone_number"));
        user.setEmail(resultSet.getString("users.email"));
        user.setRole(userRole);
        user.setRegisteredAt(resultSet.getTimestamp("users.registered_at").toLocalDateTime());
        user.setLastLoginAt(resultSet.getTimestamp("users.last_login_at").toLocalDateTime());

        return user;
    }
}
