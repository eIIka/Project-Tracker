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
    UserDTO updateUser(Long id, UserDTO userDTO);

    /**
     * Retrieves a user by ID.
     *
     * @param id the user ID.
     * @return the found user.
     */
    UserDTO getUser(Long id);

    /**
     * Deletes a user by ID.
     *
     * @param id the ID of the user to delete.
     * @return the deleted user.
     */
    UserDTO deleteUser(Long id);
}
