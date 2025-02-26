package ua.ellka.repo;

import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.model.user.User;

import java.util.Optional;

/**
 * Repository interface for handling User persistence operations.
 */
public interface UserRepo {

    /**
     * Finds a user by ID.
     *
     * @param id the ID of the user.
     * @return an Optional containing the found user or empty if not found.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    Optional<User> find(Long id) throws ProjectTrackerPersistingException;

    /**
     * Finds a user by Email.
     *
     * @param email the email of the user.
     * @return an Optional containing the found user or empty if not found.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    Optional<User> findByEmail(String email) throws ProjectTrackerPersistingException;

    /**
     * Finds a user by Username.
     *
     * @param nickname the nickname of the user.
     * @return an Optional containing the found user or empty if not found.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    Optional<User> findByNickname(String nickname) throws ProjectTrackerPersistingException;

    /**
     * Deletes a user by User.
     *
     * @param user the user to delete.
     * @return an Optional containing the deleted user or empty if not found.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    Optional<User> delete(User user) throws ProjectTrackerPersistingException;

    /**
     * Saves a user to the database.
     *
     * @param user the user to save.
     * @return an Optional containing the saved user.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    Optional<User> save(User user) throws ProjectTrackerPersistingException;

    /**
     * Updates an existing user in the database.
     *
     * @param user the user object containing updated values.
     * @return an Optional containing the updated user, or an empty Optional if the user was not found.
     * @throws ProjectTrackerPersistingException if any persisting error occurs during the update.
     */
    Optional<User> update(User user) throws ProjectTrackerPersistingException;
}
