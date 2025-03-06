package ua.ellka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ellka.dto.EmployeeDTO;
import ua.ellka.dto.ManagerDTO;
import ua.ellka.dto.UserDTO;
import ua.ellka.exception.NotFoundServiceException;
import ua.ellka.exception.ServiceException;
import ua.ellka.mapper.UserMapper;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;
import ua.ellka.model.user.User;
import ua.ellka.repo.UserRepo;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        try {
            Optional<User> userByNickname = userRepo.findByNickname(userDTO.getNickname());
            userByNickname.ifPresent(user -> {
                throw new ServiceException("User with nickname " + userDTO.getNickname() + " already exists");
            });

            Optional<User> userByEmail = userRepo.findByEmail(userDTO.getEmail());
            userByEmail.ifPresent(user -> {
                throw new ServiceException("User with email " + userDTO.getEmail() + " already exists");
            });

            return switch (userDTO.getRole()) {
                case "Manager" -> {
                    userDTO.setRole("Manager");
                    User dtoToManager = userMapper.managerDTOToManager((ManagerDTO) userDTO);

                    User manager = userRepo.save(dtoToManager);

                    yield userMapper.managerToManagerDTO((Manager) manager);
                }
                case "Employee" -> {
                    userDTO.setRole("Employee");
                    User dtoToEmployee = userMapper.employeeDTOToEmployee((EmployeeDTO) userDTO);

                    User employee = userRepo.save(dtoToEmployee);

                    yield userMapper.employeeToEmployeeDTO((Employee) employee);
                }
                default -> throw new NotFoundServiceException("User could not be created because of unknown role");
            };
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error while creating user: " + e.getMessage());
        }
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        try {
            User user = userRepo.findById(id)
                    .orElseThrow(() -> new NotFoundServiceException("User not found"));

            userRepo.findByNickname(userDTO.getNickname()).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(id)) {
                    throw new ServiceException("User with nickname " + userDTO.getNickname() + " already exists");
                }
            });

            userRepo.findByEmail(userDTO.getEmail()).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(id)) {
                    throw new ServiceException("User with email " + userDTO.getEmail() + " already exists");
                }
            });

            user.setNickname(userDTO.getNickname());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setPhoneNumber(userDTO.getPhoneNumber());
            user.setEmail(userDTO.getEmail());

            User updated = userRepo.save(user);

            return switch (userDTO.getRole()) {
                case "Manager" -> userMapper.managerToManagerDTO((Manager) updated);
                case "Employee" -> userMapper.employeeToEmployeeDTO((Employee) updated);
                default -> throw new NotFoundServiceException("User could not be updated because of unknown role");
            };
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error while updating expense by id: " + e.getMessage());
        }
    }

    @Override
    public UserDTO getUser(Long id) {
        try {
            User user = userRepo.findById(id)
                    .orElseThrow(() -> new NotFoundServiceException("User not found"));

            return switch (user.getRole().getRole()) {
                case "Manager" -> userMapper.managerToManagerDTO((Manager) user);
                case "Employee" -> userMapper.employeeToEmployeeDTO((Employee) user);
                default -> throw new NotFoundServiceException("User could not be get because of unknown role");
            };
        } catch (Exception e) {
            throw new NotFoundServiceException(e.getMessage());
        }
    }

    @Override
    public UserDTO deleteUser(Long id) {
        try {
            User user = userRepo.findById(id)
                    .orElseThrow(() -> new NotFoundServiceException("User not found"));

            userRepo.delete(user);

            return switch (user.getRole().getRole()) {
                case "Manager" -> userMapper.managerToManagerDTO((Manager) user);
                case "Employee" -> userMapper.employeeToEmployeeDTO((Employee) user);
                default -> throw new NotFoundServiceException("User could not be deleted because of unknown role");
            };
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error while deleting expense by id: " + e.getMessage());
        }
    }
}
