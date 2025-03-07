package ua.ellka.service;

import ua.ellka.dto.AuthDTO;
import ua.ellka.dto.LoginDTO;
import ua.ellka.dto.RegisterDTO;
import ua.ellka.dto.UserDTO;

public interface AuthService {
    UserDTO registerUser(RegisterDTO registerDTO);
    AuthDTO authenticateUser(LoginDTO loginDTO);
    UserDTO getCurrentUser();

}
