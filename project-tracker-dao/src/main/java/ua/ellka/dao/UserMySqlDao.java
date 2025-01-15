package ua.ellka.dao;

import lombok.Data;
import ua.ellka.exception.DaoException;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;
import ua.ellka.model.user.User;
import ua.ellka.model.user.UserRole;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class UserMySqlDao implements UserDao {
    private final Connection connection;

    @Override
    public List<User> findAll() throws DaoException {
        try (Statement statement = connection.createStatement()) {

            boolean execute = statement.execute("SELECT * FROM users");
            if (!execute) {
                throw new DaoException("Can't find all users");
            }

            List<User> users = new ArrayList<>();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                User user;
                String roleString = resultSet.getString("user_role");
                UserRole userRole = UserRole.fromString(roleString);

                if (userRole == UserRole.MANAGER) {
                    user = new Manager();
                } else {
                    user = new Employee();
                }

                user.setId(resultSet.getLong("id"));
                user.setNickname(resultSet.getString("nickname"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setPhoneNumber(resultSet.getString("phone_number"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(userRole);
                user.setRegisteredAt(resultSet.getObject("registered_at", LocalDateTime.class));
                user.setLastLoginAt(resultSet.getObject("last_login_at", LocalDateTime.class));
                users.add(user);
            }

            return users;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> findById(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            statement.setLong(1, id);

            if (!statement.execute()) {
                return Optional.empty();
            }

            ResultSet resultSet = statement.getResultSet();
            if (!resultSet.next()){
                return Optional.empty();
            }

            return Optional.of(mapResultSetToUser(resultSet));

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> findByNickname(String nickname) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE nickname = ?")) {
            statement.setString(1, nickname);

            if (!statement.execute()) {
                return Optional.empty();
            }

            ResultSet resultSet = statement.getResultSet();
            if (!resultSet.next()){
                return Optional.empty();
            }

            return Optional.of(mapResultSetToUser(resultSet));

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ?")) {
            statement.setString(1, email);

            if (!statement.execute()) {
                return Optional.empty();
            }

            ResultSet resultSet = statement.getResultSet();
            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(mapResultSetToUser(resultSet));

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> deleteById(Long id) throws DaoException {
        Optional<User> userToDelete = findById(id);
        if (userToDelete.isEmpty()) {
            return Optional.empty();
        }

        try(PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?")){
            statement.setLong(1, id);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return userToDelete;
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> deleteByNickname(String nickname) throws DaoException {
        Optional<User> userToDelete = findByNickname(nickname);
        if (userToDelete.isEmpty()) {
            return Optional.empty();
        }

        try(PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE nickname = ?")){
            statement.setString(1, nickname);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return userToDelete;
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> deleteByEmail(String email) throws DaoException {
        Optional<User> userToDelete = findByEmail(email);
        if (userToDelete.isEmpty()) {
            return Optional.empty();
        }

        try(PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE email = ?")){
            statement.setString(1, email);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return userToDelete;
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> save(User user) throws DaoException {
        try (PreparedStatement statement =
                     connection.prepareStatement(
                             "INSERT INTO users(nickname, first_name, last_name, phone_number, email, password, user_role) VALUES (?, ?, ?, ?, ?, ?, ?)",
                             Statement.RETURN_GENERATED_KEYS
                     )
        ) {

            statement.setString(1, user.getNickname());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getPhoneNumber());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPassword());
            statement.setString(7, user.getRole().toString());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                return Optional.empty();
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong(1));
            }

            return Optional.of(user);

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> updateById(Long id, User user) throws DaoException {
        Optional<User> userToUpdate = findById(id);
        if (userToUpdate.isEmpty()) {
            return Optional.empty();
        }

        try(PreparedStatement statement =
                    connection.prepareStatement(
                            "UPDATE users SET nickname = ?, first_name = ?, last_name = ?, phone_number = ?, email = ?, password = ?, user_role = ? WHERE id = ?"
                    )
        ){
            statement.setString(1, user.getNickname());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getPhoneNumber());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPassword());
            statement.setString(7, user.getRole().toString());
            statement.setLong(8, id);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return Optional.of(user);
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return Optional.empty();
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        String roleString = resultSet.getString("user_role");
        UserRole userRole = UserRole.fromString(roleString);

        User user;
        if (userRole == UserRole.MANAGER) {
            user = new Manager();
        } else {
            user = new Employee();
        }

        user.setId(resultSet.getLong("id"));
        user.setNickname(resultSet.getString("nickname"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setPhoneNumber(resultSet.getString("phone_number"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(userRole);
        user.setRegisteredAt(resultSet.getObject("registered_at", LocalDateTime.class));
        user.setLastLoginAt(resultSet.getObject("last_login_at", LocalDateTime.class));

        return user;
    }

}
