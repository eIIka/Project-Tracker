package ua.ellka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.ellka.dto.EmployeeDTO;
import ua.ellka.dto.ManagerDTO;
import ua.ellka.dto.UserDTO;
import ua.ellka.exception.ProjectTrackerPersistingException;
import ua.ellka.mapper.UserMapper;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;
import ua.ellka.model.user.User;
import ua.ellka.model.user.UserRole;
import ua.ellka.repo.UserRepo;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        try {
            if (userRepo.findByEmail(userDTO.getEmail()).isPresent() || userRepo.findByNickname(userDTO.getNickname()).isPresent()) {
                return null;
            }

            return switch (userDTO.getRole()) {
                case "Manager" -> {
                    User manager = userMapper.managerDTOToManager((ManagerDTO) userDTO);
                    Optional<User> managerOptional = userRepo.save(manager);
                    yield userMapper.managerToManagerDTO((Manager) managerOptional.get());
                }
                case "Employee" -> {
                    User employee = userMapper.employeeDTOToEmployee((EmployeeDTO) userDTO);
                    Optional<User> employeeOptional = userRepo.save(employee);
                    yield userMapper.employeeToEmployeeDTO((Employee) employeeOptional.get());
                }
                default -> null;
            };

        } catch (ProjectTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        try{
            if (userRepo.findByNickname(userDTO.getNickname()).isPresent() || userRepo.findByEmail(userDTO.getEmail()).isPresent()) {
                return null;
            }

            return switch (userDTO.getRole()) {
                case "Manager" -> {
                    User manager = userMapper.managerDTOToManager((ManagerDTO) userDTO);
                    Optional<User> managerOptional = userRepo.update(manager);
                    yield userMapper.managerToManagerDTO((Manager) managerOptional.get());
                }
                case "Employee" -> {
                    User employee = userMapper.employeeDTOToEmployee((EmployeeDTO) userDTO);
                    Optional<User> employeeOptional = userRepo.update(employee);
                    yield userMapper.employeeToEmployeeDTO((Employee) employeeOptional.get());
                }
                default -> null;
            };

        } catch (ProjectTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDTO getUser(Long id) {
        try {
            User user = userRepo.find(id).get();

            return switch (user.getRole().getRole()) {
                case "Manager" -> {
                    Optional<User> managerOptional = userRepo.update(user);
                    yield userMapper.managerToManagerDTO((Manager) managerOptional.get());
                }
                case "Employee" -> {
                    Optional<User> employeeOptional = userRepo.update(user);
                    yield userMapper.employeeToEmployeeDTO((Employee) employeeOptional.get());
                }
                default -> null;
            };
        } catch (ProjectTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public List<UserDTO> getAllUsers() {
//        return List.of();
//    }

    @Override
    public UserDTO deleteUser(Long id) {
        try {
            User user = userRepo.find(id).get();
            if (user == null) {
                throw new ProjectTrackerPersistingException(userRepo.find(id).get(), "User not found", null);
            }

            Optional<User> deleted = userRepo.delete(user.getId());

            return switch (deleted.get().getRole().getRole()) {
                case "Manager" -> userMapper.managerToManagerDTO((Manager) deleted.get());

                case "Employee" -> userMapper.employeeToEmployeeDTO((Employee) deleted.get());

                default -> null;
            };
        } catch (ProjectTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean assignRoleToUser(Long id, String role) {
        try {
            Optional<User> userOptional = userRepo.find(id);
            if (userOptional.isEmpty()){
                return false;
            }

            userOptional.get().setRole(UserRole.valueOf(role));
            userRepo.update(userOptional.get());
            return true;

        } catch (ProjectTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean removeRoleFromUser(Long id, String role) {
        try {
            Optional<User> userOptional = userRepo.find(id);
            if (userOptional.isEmpty()){
                return false;
            }

            userOptional.get().setRole(null);
            userRepo.update(userOptional.get());
            return true;

        } catch (ProjectTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }
}
