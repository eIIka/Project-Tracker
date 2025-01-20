package ua.ellka.repo;

import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.model.user.User;

import java.util.List;
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
    Optional<User> find(String id) throws ProjectTrackerPersistingException;

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
     * @param username the username of the user.
     * @return an Optional containing the found user or empty if not found.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    Optional<User> findByUsername(String username) throws ProjectTrackerPersistingException;

    /**
     * Deletes a user by ID.
     *
     * @param id the ID of the user to delete.
     * @return an Optional containing the deleted user or empty if not found.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    Optional<User> delete(Long id) throws ProjectTrackerPersistingException;

    /**
     * Saves a user to the database.
     *
     * @param user the user to save.
     * @return an Optional containing the saved user.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    Optional<User> save(User user) throws ProjectTrackerPersistingException;

    /**
     * Updates a user's information in the database.
     *
     * @param user the user with updated information.
     * @return an Optional containing the updated user or empty if the user was not found.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    Optional<User> update(User user) throws ProjectTrackerPersistingException;

    /**
     * Finds all users in the system.
     *
     * @return a List of all users.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    List<User> findAll() throws ProjectTrackerPersistingException;

    /**
     * Checks if a user exists by ID.
     *
     * @param id the ID of the user.
     * @return true if the user exists, false otherwise.
     * @throws ProjectTrackerPersistingException if any persisting error occurs.
     */
    boolean existsById(Long id) throws ProjectTrackerPersistingException;

}
