package ua.ellka.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.ellka.exception.DaoException;
import ua.ellka.mapper.UserRowMapper;
import ua.ellka.model.user.User;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserSpringJdbcDao implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Override
    public List<User> findAll() throws DaoException {
        return jdbcTemplate.query("SELECT * FROM users", userRowMapper);
    }

    @Override
    public Optional<User> findById(Long id) throws DaoException {
        User user = jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id = ?",
                new Object[]{id},
                new int[]{Types.INTEGER},
                userRowMapper);

        return Optional.of(user);
    }

    @Override
    public Optional<User> findByNickname(String nickname) throws DaoException {
        User user = jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE nickname = ?",
                new Object[]{nickname},
                new int[]{Types.VARCHAR},
                userRowMapper);

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByEmail(String email) throws DaoException {
        User user = jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE email = ?",
                new Object[]{email},
                new int[]{Types.VARCHAR},
                userRowMapper);

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> deleteById(Long id) throws DaoException {
        Optional<User> foundUser = findById(id);
        if (foundUser.isEmpty()) {
            throw new DaoException("User with id " + id + " not found");
        }

        int deletedRows = jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);

        return deletedRows > 0 ? foundUser : Optional.empty();
    }

    @Override
    public Optional<User> deleteByNickname(String nickname) throws DaoException {
        Optional<User> foundUser = findByNickname(nickname);
        if (foundUser.isEmpty()) {
            throw new DaoException("User with nickname " + nickname + " not found");
        }

        int deletedRows = jdbcTemplate.update("DELETE FROM users WHERE nickname = ?", nickname);

        return deletedRows > 0 ? foundUser : Optional.empty();
    }

    @Override
    public Optional<User> deleteByEmail(String email) throws DaoException {
        Optional<User> foundUser = findByEmail(email);
        if (foundUser.isEmpty()) {
            throw new DaoException("User with email " + email + " not found");
        }

        int deletedRows = jdbcTemplate.update("DELETE FROM users WHERE email = ?", email);

        return deletedRows > 0 ? foundUser : Optional.empty();
    }

    @Override
    public Optional<User> save(User user) throws DaoException {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(
                    "INSERT INTO users(nickname, first_name, last_name, phone_number, email, password, user_role) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, user.getNickname());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getPhoneNumber());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getPassword());
            preparedStatement.setString(7, user.getRole().toString());

            return preparedStatement;
        }, keyHolder);

        if (rowsAffected == 0) {
            throw new DaoException("Could not save user");
        }

        long savedUserId = (Integer) keyHolder.getKeyList()
                .get(0)
                .get("ID");

        user.setId(savedUserId);

        return Optional.of(user);
    }

    @Override
    public Optional<User> updateById(Long id, User user) throws DaoException {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        Optional<User> userToUpdate = findById(id);
        if (userToUpdate.isEmpty()) {
            throw new DaoException("User with id " + id + " not found");
        }

        int rowsAffected = jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement("""
                    UPDATE users SET nickname = ?, first_name = ?, last_name = ?, phone_number = ?, email = ?, password = ?, user_role = ? WHERE id = ?
                    """, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getNickname());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getPhoneNumber());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getPassword());
            preparedStatement.setString(7, user.getRole().toString());
            preparedStatement.setLong(8, id);

            return preparedStatement;
        });

        if (rowsAffected == 0) {
            throw new DaoException("Could not update user");
        }

        user.setId(id);

        return Optional.of(user);
    }
}
