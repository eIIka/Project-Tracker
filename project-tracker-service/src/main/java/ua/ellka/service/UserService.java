package ua.ellka.service;

import ua.ellka.dto.UserDTO;

import java.util.List;

public interface UserService {

    /**
     * Creates a new user.
     *
     * @param userDTO the user data transfer object.
     * @return the created user.
     */
    UserDTO createUser(UserDTO userDTO);

    /**
     * Updates an existing user.
     *
     * @param userDTO the user data transfer object with updated information.
     * @return the updated user.
     */
    UserDTO updateUser(UserDTO userDTO);

    /**
     * Retrieves a user by ID.
     *
     * @param id the user ID.
     * @return the found user.
     */
    UserDTO getUser(Long id);

    /**
     * Retrieves all users.
     *
     * @return a list of all users.
     */
    //List<UserDTO> getAllUsers();

    /**
     * Deletes a user by ID.
     *
     * @param id the ID of the user to delete.
     * @return the deleted user.
     */
    UserDTO deleteUser(Long id);

    /**
     * Assigns a role to a user.
     *
     * @param id the user ID.
     * @param role the role to assign.
     * @return true if the role was successfully assigned, false otherwise.
     */
    boolean assignRoleToUser(Long id, String role);

    /**
     * Removes a role from a user.
     *
     * @param id the user ID.
     * @param role the role to remove.
     * @return true if the role was successfully removed, false otherwise.
     */
    boolean removeRoleFromUser(Long id, String role);
}
