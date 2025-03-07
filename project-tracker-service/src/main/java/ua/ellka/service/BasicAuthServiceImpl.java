package ua.ellka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.ellka.dto.AuthDTO;
import ua.ellka.dto.LoginDTO;
import ua.ellka.dto.RegisterDTO;
import ua.ellka.dto.UserDTO;
import ua.ellka.exception.NotFoundServiceException;
import ua.ellka.exception.ServiceException;
import ua.ellka.mapper.UserMapper;
import ua.ellka.model.security.ProjectTrackerUserDetails;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;
import ua.ellka.model.user.User;
import ua.ellka.repo.UserRepo;

@Service
@RequiredArgsConstructor
public class BasicAuthServiceImpl implements AuthService {
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDTO registerUser(RegisterDTO registerDTO) {
        String passwordHash = passwordEncoder.encode(registerDTO.getPassword());

        User user;
        User saved;
        return switch (registerDTO.getRole()) {
            case "Employee" -> {
                user = new Employee();
                user.setNickname(registerDTO.getNickname());
                user.setFirstName(registerDTO.getFirstName());
                user.setLastName(registerDTO.getLastName());
                user.setEmail(registerDTO.getEmail());
                user.setPassword(passwordHash);

                saved = userRepo.save(user);
                yield userMapper.employeeToEmployeeDTO((Employee) saved);
            }
            case "Manager" -> {
                user = new Manager();
                user.setNickname(registerDTO.getNickname());
                user.setFirstName(registerDTO.getFirstName());
                user.setLastName(registerDTO.getLastName());
                user.setEmail(registerDTO.getEmail());
                user.setPassword(passwordHash);

                saved = userRepo.save(user);
                yield userMapper.managerToManagerDTO((Manager) saved);
            }
            default -> throw new NotFoundServiceException("Invalid role");
        };
    }

    @Override
    public AuthDTO authenticateUser(LoginDTO loginDTO) {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public UserDTO getCurrentUser() {
        try {
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();

            Object principal = authentication.getPrincipal();

            Long userId = switch (principal) {
                case ProjectTrackerUserDetails projectTrackerPrincipal -> projectTrackerPrincipal.getId();
                default -> throw new ServiceException("Anonymous user");
            };

            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new NotFoundServiceException("User not found"));

            return switch (user.getRole().getRole()){
                case "Manager" -> userMapper.managerToManagerDTO((Manager) user);
                case "Employee" -> userMapper.employeeToEmployeeDTO((Employee) user);
                default -> throw new NotFoundServiceException("Invalid role");
            };
        } catch (Exception e) {
            throw new NotFoundServiceException("User not found");
        }
    }
}
