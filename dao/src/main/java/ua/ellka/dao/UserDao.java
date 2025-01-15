package ua.ellka.dao;

import ua.ellka.exception.DaoException;
import ua.ellka.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> findAll() throws DaoException;
    Optional<User> findById(Long id) throws DaoException;
    Optional<User> findByNickname(String nickname) throws DaoException;
    Optional<User> findByEmail(String email) throws DaoException;

    Optional<User> deleteById(Long id) throws DaoException;
    Optional<User> deleteByNickname(String nickname) throws DaoException;
    Optional<User> deleteByEmail(String email) throws DaoException;

    Optional<User> save(User user) throws DaoException;

    Optional<User> updateById(Long id, User user) throws DaoException;


}
