package ua.ellka.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ua.ellka.dto.AdminDTO;
import ua.ellka.dto.EmployeeDTO;
import ua.ellka.dto.ManagerDTO;
import ua.ellka.dto.UserDTO;
import ua.ellka.model.user.Admin;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;
import ua.ellka.model.user.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "role", source = "role")
    default UserDTO UserToUserDTO(User user) {
        return switch (user.getRole()) {
            case ADMIN -> AdminDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .role("ADMIN")
                    .build();
            case MANAGER -> ManagerDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .role("MANAGER")
                    .build();
            case EMPLOYEE -> EmployeeDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .role("EMPLOYEE")
                    .build();
            default -> throw new IllegalArgumentException("Unknown role: " + user.getRole());
        };
    }

    @Mapping(target = "role", source = "role")
    default User UserDTOToUser(UserDTO dto) {
        return switch (dto.getRole()) {
            case "ADMIN" ->  Admin.builder().build();
            case "MANAGER" -> Manager.builder().build();
            case "EMPLOYEE" -> Employee.builder().build();
            default -> throw new IllegalArgumentException("Unknown role: " + dto.getRole());
        };
    }
}
